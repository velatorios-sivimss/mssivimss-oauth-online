package com.imss.sivimss.oauth.util;

/**
 * Clase para la paginacion
 *
 * @author    
 * @puesto dev
 * @date 24 nov. 2022
 */
public class AppConstantes {
	
	public static final String NUMERO_DE_PAGINA = "0";
	public static final String TAMANIO_PAGINA = "10";
	public static final String ORDER_BY= "id";
	public static final String ORDER_DIRECTION= "asc";
	public static final String SUPERVISOR = "Supervisor";
	
	public static final String DATOS= "datos";
	public static final String QUERY= "query";
	public static final String STATUSEXCEPTION = "status";
	public static final String EXPIREDJWTEXCEPTION = "expired";
	public static final String MALFORMEDJWTEXCEPTION = "malformed";
	public static final String UNSUPPORTEDJWTEXCEPTION = "unsupported";
	public static final String ILLEGALARGUMENTEXCEPTION  = "illegalArgument";
	public static final String SIGNATUREEXCEPTION  = "signature";
	public static final String FORBIDDENEXCEPTION  = "forbidden";
	
	public static final String EXPIREDJWTEXCEPTION_MENSAJE = "Token expirado.";
	public static final String MALFORMEDJWTEXCEPTION_MENSAJE = "Token mal formado.";
	public static final String UNSUPPORTEDJWTEXCEPTION_MENSAJE = "Token no soportado.";
	public static final String ILLEGALARGUMENTEXCEPTION_MENSAJE  = "Token vacío.";
	public static final String SIGNATUREEXCEPTION_MENSAJE  = "Fallo la firma.";
	public static final String FORBIDDENEXCEPTION_MENSAJE  = "No tiene autorización para realizar la solicitud.";

	public static final String USUARIO= "usuario";
	public static final String CONTRASENIA= "contrasenia";
	public static final String CONTRASENIA_ANTERIOR= "contraseniaAnterior";
	public static final String CONTRASENIA_NUEVA= "contraseniaNueva";
	public static final String CODIGO= "codigo";

	public static final String CIRCUITBREAKER = "El servicio no responde, no permite más llamadas.";
	
	public static final String IDROL = "idRol";
	
	public static final String CONTRASENIA_PROX_VENCER= "contraseniaProximaVencer";
	
	public static final String ID_DELEGACION= "idDelegacion";
	
	public static final String SIAP_ACTIVO= "ACTIVO";
	
	public static final String TIPO_CORREO= "recuperarContrasenia";
	
	public static final String ALTA = "alta";
	public static final String BAJA = "baja";
	public static final String MODIFICACION = "modificacion";
	public static final String CONSULTA = "consulta";
	
	
	private AppConstantes() {
	    throw new IllegalStateException("AppConstantes class");
	  }

}
