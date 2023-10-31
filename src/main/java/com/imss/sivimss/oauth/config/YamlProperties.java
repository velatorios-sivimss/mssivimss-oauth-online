package com.imss.sivimss.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.imss.sivimss.oauth.service.impl.YamlPropertySourceFactory;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@Data
public class YamlProperties {

	private String url;
	
}
