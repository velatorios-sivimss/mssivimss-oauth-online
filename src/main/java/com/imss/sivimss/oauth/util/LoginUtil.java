package com.imss.sivimss.oauth.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginUtil {
	
	private static final String SVT_LOGIN = "SVT_LOGIN";
	
	public String buscarPorIdUsuario(String idUsuario) {
		
		StringBuilder query = new StringBuilder("SELECT ");
		query.append( "* " );
		query.append( "FROM "+ SVT_LOGIN  );
		query.append( " WHERE ID_USUARIO = ");
		query.append( idUsuario + " ");
		query.append( BdConstantes.LIMIT );
		
		return query.toString();
	}
	
	
	public String insertar(String idUsuario) {
		
		StringBuilder query = new StringBuilder("INSERT INTO ");
		query.append( SVT_LOGIN + "(`ID_USUARIO`) VALUES " );
		query.append( "('" +idUsuario + "')");
		
		return query.toString();
	}
	
	public String buscarPorCveUsuario(String cveUsuario) {
		
		StringBuilder query = new StringBuilder(BdConstantes.SELECT);
		query.append( "LOGIN.* " );
		query.append( "FROM " + SVT_LOGIN + " LOGIN " );
		query.append( "INNER JOIN SVT_USUARIOS US ON US.ID_USUARIO = LOGIN.ID_USUARIO " );
		query.append( "WHERE US.CVE_USUARIO = ");
		query.append( "'" + cveUsuario + "' ");
		query.append( BdConstantes.LIMIT );
		
		return query.toString();
	}
	
	public List<String> actContrasenia( String idLogin, String idUsuario, String contrasenia ) {
		
		List<String> lista = new ArrayList<>();
		StringBuilder query = new StringBuilder(BdConstantes.UPDATE);
		query.append( "SVT_USUARIOS " );
		query.append( "SET `CVE_CONTRASENIA` = '"+ contrasenia +"', " );
		query.append( "`FEC_ACTUALIZACION` = NOW(), " );
		query.append( "`ID_USUARIO_MODIFICA` = '"+ idUsuario +"' " );
		query.append( "WHERE (`ID_USUARIO` = '"+ idUsuario +"') " );
		
		lista.add( query.toString() );
		
		query = new StringBuilder(BdConstantes.UPDATE);
		query.append( SVT_LOGIN + " " );
		query.append( "SET `FEC_CAMBIO_CONTRASENIA` = NOW(), " );
		query.append( "`CVE_ESTATUS_CUENTA` = '"+ BdConstantes.ESTATUS_ACTIVO +"' " );
		query.append( ", `FEC_BLOQUEO` = null " );
		query.append( "WHERE (`ID_LOGIN` = '"+ idLogin +"') " );
		
		lista.add( query.toString() );
		
		return lista;
	}
	
	public String actNumIntentos( String idLogin, Integer numIntentos, Integer maxNumIntentos ) {
		
		StringBuilder query = new StringBuilder(BdConstantes.UPDATE);
		query.append( SVT_LOGIN + " " );
		query.append( "SET `NUM_INTENTOS` = " + numIntentos );
		
		if( numIntentos >= maxNumIntentos ) {
			query.append( ", `FEC_BLOQUEO` = NOW() " );
		}else {
			query.append( ", `FEC_BLOQUEO` = null " );
		}
		
		query.append( " WHERE (`ID_LOGIN` = '"+ idLogin +"') " );
		
		return query.toString();
	}
	
	public String generarCodigo( Integer longitud ) {
		
		StringBuilder query = new StringBuilder();
		Random random = new SecureRandom();
		Integer numAleatorio;
		
		for(int i=0; i<longitud; i++) {
			
			numAleatorio = random.nextInt(10);
			query.append( numAleatorio );
		}
		
		return query.toString();
	}
	
	public String actCodSeg(String idLogin, String codigo) {
		
		StringBuilder query = new StringBuilder(BdConstantes.UPDATE);
		query.append( SVT_LOGIN + " " );
		query.append( "SET `CVE_CODIGO_SEGURIDAD` = '" + codigo + "'" );
		query.append( ", `FEC_CODIGO_SEGURIDAD` = NOW() " );
		query.append( " WHERE (`ID_LOGIN` = '"+ idLogin +"') " );

		return query.toString();
		
	}
	
	public String difTiempo(String idLogin) {
		
		StringBuilder query = new StringBuilder("SELECT\r\n"
				+ "LOGIN.ID_LOGIN AS idLogin,\r\n"
				+ "LOGIN.FEC_CODIGO_SEGURIDAD AS fecCodigoSeguridad,\r\n"
				+ "CURRENT_TIMESTAMP() AS tiempoActual,\r\n"
				+ "TIMESTAMPDIFF(MINUTE,LOGIN.FEC_CODIGO_SEGURIDAD,CURRENT_TIMESTAMP()) AS diferencia\r\n"
				+ "FROM     SVT_LOGIN LOGIN\r\n"
				+ "WHERE     LOGIN.ID_LOGIN = '");
		query.append( idLogin );
		query.append("'\r\n"
				+ "LIMIT     1");
		
		return query.toString();
		
	}
	
	public String historial(String idLogin, String codigo) {
		
		QueryHelper q = new QueryHelper("INSERT INTO SVT_HIST_CODIGO_SEGURIDAD");
		q.agregarParametroValues("ID_LOGIN", "'" + idLogin + "'");
		q.agregarParametroValues("CVE_CODIGO_SEGURIDAD", "'" + codigo + "'");
		q.agregarParametroValues("FEC_CODIGO_SEGURIDAD", "NOW()");
		
		return q.obtenerQueryInsertar();
		
	}
	
	public String conteo(String idLogin, String codigo) {
		
		StringBuilder query = new StringBuilder("SELECT \r\n"
				+ "count( ID_HIST_CODIGO_SEGURIDAD ) AS conteo\r\n"
				+ "FROM\r\n"
				+ "SVT_HIST_CODIGO_SEGURIDAD\r\n"
				+ "WHERE\r\n"
				+ "ID_LOGIN = '");
		query.append( idLogin );
		query.append("'\r\n"
				+ "AND CVE_CODIGO_SEGURIDAD = '");
		query.append( codigo );
		query.append("'");
		
		return query.toString();
		
	}
	
	public String difTiempoBloqueo(String idLogin) {
		
		StringBuilder query = new StringBuilder("SELECT\r\n"
				+ "LOGIN.ID_LOGIN AS idLogin,\r\n"
				+ "LOGIN.FEC_BLOQUEO AS fecBloqueo,\r\n"
				+ "CURRENT_TIMESTAMP() AS tiempoActual,\r\n"
				+ "TIMESTAMPDIFF(MINUTE,LOGIN.FEC_BLOQUEO,CURRENT_TIMESTAMP()) AS diferencia\r\n"
				+ "FROM     SVT_LOGIN LOGIN\r\n"
				+ "WHERE     LOGIN.ID_LOGIN ='");
		query.append( idLogin );
		query.append("'\r\n"
				+ "LIMIT     1");
		
		return query.toString();
		
	}
	
}
