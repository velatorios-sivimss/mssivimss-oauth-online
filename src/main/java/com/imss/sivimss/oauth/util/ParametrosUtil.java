package com.imss.sivimss.oauth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParametrosUtil {

	private Logger log = LoggerFactory.getLogger(ParametrosUtil.class);
	
	private static final String QUERY_INICIO = "SELECT TIP_PARAMETRO FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO = ";
	private static final String QUERY_FINAL = " AND IND_ACTIVO = '1' LIMIT 1";
	
	public String tiempoToken() {
		return armarQuery("TIEMPO TOKEN");
	}
	
	public String consultarSiap() {
		return armarQuery("CONSULTAR SIAP");
	}
	
	public String numDias() {
		return armarQuery("NUM DIAS A CADUCAR");
	}
	
	public String numMeses() {
		return armarQuery("NUM MESES VIGENCIA");
	}
	
	public String numIntentos() {
		return armarQuery("NUM MAXIMO DE INTENTOS");
	}
	
	public String tiempoBloqueo() {
		return armarQuery("MINUTOS DE BLOQUEO");
	}
	
	public String longCodigo() {
		return armarQuery("LONGITUD CODIGO NUMERICO");
	}
	
	public String tiempoCodigo() {
		return armarQuery("TIEMPO CODIGO NUMERICO");
	}
	
	public String numNiveles() {
		return armarQuery("NUM NIVELES");
	}
	
	private String armarQuery(String param) {
		
		StringBuilder query = new StringBuilder( QUERY_INICIO );
		query.append( "'" + param + "'");
		query.append( QUERY_FINAL );
		log.info( query.toString() );
		return query.toString();
	}
	
	public String obtenerFecha(String formato) {
		StringBuilder query = new StringBuilder( "SELECT DATE_FORMAT(CURRENT_TIMESTAMP, '" );
		query.append( formato );
		query.append( "') AS tiempo" );
		log.info( query.toString() );
		return query.toString();
	}
}
