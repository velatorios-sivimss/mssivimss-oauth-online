package com.imss.sivimss.oauth.util;

/**
 * Clase para la paginacion
 *
 * @author    
 * @puesto dev
 * @date 24 nov. 2022
 */
public class BdConstantes {
	
	public static final String SELECT_CONTRATANTE = "SELECT * FROM SVC_CONTRATANTE SC ";
	public static final String PAIS = "SELECT ID_PAIS AS idPais, DES_PAIS AS pais FROM SVC_PAIS";
	public static final String EDO = "SELECT ID_ESTADO AS idEstado, DES_ESTADO AS estado FROM SVC_ESTADO";
	public static final String WHERE = "WHERE ";
	public static final String AND = "AND ";
	public static final String CVE_USUARIO= "CVE_USUARIO";
	public static final String ACTIVO= "IND_ACTIVO = '1' ";
	public static final String LIMIT= "LIMIT 1 ";
	public static final String RN= "'\r\n";
	public static final String SELECT= "SELECT ";
	public static final String UPDATE= "UPDATE ";
	
	public static final String ESTATUS_ACTIVO= "ACTIVO";
	public static final String ESTATUS_PRE_ACTIVO= "PRE ACTIVO";
	public static final String ESTATUS_DESACTIVADO= "DESACTIVADA";
	
	public static final String TIP_PARAMETRO= "TIP_PARAMETRO";
	
	public static final String REF_CORREO = "REF_CORREO"; 
	public static final String CVE_CONTRASENIA = "CVE_CONTRASENIA"; 
	public static final String CVE_MATRICULA = "CVE_MATRICULA"; 
	public static final String ID_ESTADO = "ID_ESTADO";
	public static final String FEC_ALTA = "FEC_ALTA";
	public static final String CURRENT_DATE = "CURRENT_DATE()";
	public static final String MAX_CONTRATANTE = "SELECT MAX(ID_CONTRATANTE)+1 AS max FROM SVC_CONTRATANTE"; 
	
	private BdConstantes() {
	    throw new IllegalStateException("BdConstantes class");
	  }

}
