package com.imss.sivimss.oauth;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import com.imss.sivimss.oauth.util.NoRedirectSimpleClientHttpRequestFactory;

@SpringBootApplication
@EnableCaching
public class OauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthApplication.class);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().permitAll();
		return http.build();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper getMapper(){
	return new ModelMapper();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder().requestFactory(NoRedirectSimpleClientHttpRequestFactory.class)
				.setConnectTimeout(Duration.ofMillis(195000)).setReadTimeout(Duration.ofMillis(195000)).build();
	}
}
