package com.imss.sivimss.oauth.config;

import java.io.IOException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.imss.sivimss.oauth.config.mymapper.Consultas;

@Component
public class MyBatisConnect {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MyBatisConnect.class);
	
	private AnnotationConfigApplicationContext context = null;
	
	public AnnotationConfigApplicationContext conectar () throws IOException {
		try {
			context = new AnnotationConfigApplicationContext(MyBatisConfig.class);
			return context;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	public Consultas crearBeanDeConsultas () throws IOException {
		Consultas consultas = null;
		
		try {
			consultas = context.getBean(Consultas.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			return consultas;
		}
		
		return consultas;
	}
}
