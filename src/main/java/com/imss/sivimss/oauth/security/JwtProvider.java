package com.imss.sivimss.oauth.security;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	
	@Value("${jwt.secretkey}")
	private String secret;


	public String createToken(String json, Long tiempo) {
		
		Map<String, Object> claims = Jwts.claims().setSubject(json);
		Date now = new Date();
		Date exp = new Date(now.getTime() + tiempo);
		return Jwts.builder()
				.setHeaderParam("sistema", "sivimss").setClaims(claims).setIssuedAt(now).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public void validate(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
	}

	public String getUsernameFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token ");
		}
	}
}
