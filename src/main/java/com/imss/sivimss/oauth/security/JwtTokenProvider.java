package com.imss.sivimss.oauth.security;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secretkey-flujo}")
	private String jwtSecret;
	@Value("${jwt.secretkey-dominios}")
	private String jwtSecretDominios;

	@Value("${jwt.expiration-milliseconds}")
	private String expiration;

	public String createToken(String subject) {
		Map<String, Object> claims = Jwts.claims().setSubject(subject);
		Date now = new Date();
		Date exp = new Date(now.getTime() + Long.parseLong(expiration) * 1000);
		return Jwts.builder().setHeaderParam("sistema", "sivimss").setClaims(claims).setIssuedAt(now).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, jwtSecretDominios).compact();
	}
	
	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

}
