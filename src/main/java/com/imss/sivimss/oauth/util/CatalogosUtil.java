package com.imss.sivimss.oauth.util;

import com.imss.sivimss.oauth.service.impl.CatalogosServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CatalogosUtil {
	
	public String delegacion() {
		
		StringBuilder query = new StringBuilder("SELECT ID_DELEGACION as id, ");
		query.append( "DES_DELEGACION as 'desc' " );
		query.append( "FROM SVC_DELEGACION " );
		query.append( "ORDER BY ID_DELEGACION ASC " );
		
		return query.toString();
		
	}
	
	public String nivelOficina() {
		
		StringBuilder query = new StringBuilder("SELECT ID_OFICINA AS id, ");
		query.append( "DES_NIVELOFICINA AS 'desc' " );
		query.append( "FROM SVC_NIVEL_OFICINA " );
		query.append( "ORDER BY ID_OFICINA ASC " );
		
		return query.toString();
	}
	
	public String velatorios(String idDelegacion) {
		
		StringBuilder query = new StringBuilder("SELECT ID_VELATORIO AS id, ");
		query.append( "DES_VELATORIO AS 'desc' " );
		query.append( "FROM SVC_VELATORIO " );
		
		if( idDelegacion!=null && !idDelegacion.equalsIgnoreCase("null")) {
			query.append( "WHERE ");
			query.append( "ID_DELEGACION = " +  idDelegacion);
		}
		
		query.append( " ORDER BY ID_VELATORIO ASC " );
		
		return query.toString();
	}
	
	public String parentesco() {
		
		StringBuilder query = new StringBuilder("SELECT ID_PARENTESCO AS id, ");
		query.append( "DES_PARENTESCO AS 'desc' " );
		query.append( "FROM SVC_PARENTESCO " );
		query.append( "ORDER BY ID_PARENTESCO ASC " );
		
		return query.toString();
	}
	
	public String pais() {
		
		StringBuilder query = new StringBuilder("SELECT ID_PAIS AS id, ");
		query.append( "DES_PAIS AS 'desc' " );
		query.append( "FROM SVC_PAIS " );
		query.append( "ORDER BY ID_PAIS ASC " );
		
		return query.toString();
	}
	
	public String estados() {
		
		StringBuilder query = new StringBuilder("SELECT ID_ESTADO AS id, ");
		query.append( "DES_ESTADO AS 'desc' " );
		query.append( "FROM SVC_ESTADO " );
		query.append( "ORDER BY ID_ESTADO ASC " );
		
		return query.toString();
	}
	
	public String tipoOrden() {
		
		StringBuilder query = new StringBuilder("SELECT ID_TIPO_ORDEN_SERVICIO AS id, ");
		query.append( "DES_TIPO_ORDEN_SERVICIO AS 'desc' " );
		query.append( "FROM SVC_TIPO_ORDEN_SERVICIO " );
		query.append( "ORDER BY ID_TIPO_ORDEN_SERVICIO ASC " );
		
		return query.toString();
	}
	
	public String tipoPension() {
		
		StringBuilder query = new StringBuilder("SELECT ID_TIPO_PENSION AS id, ");
		query.append( "DES_PENSION AS 'desc' " );
		query.append( "FROM SVC_TIPO_PENSION " );
		query.append( "ORDER BY ID_TIPO_PENSION ASC " );
		
		return query.toString();
	}
	
	public String unidadesMedicas() {
		
		StringBuilder query = new StringBuilder("SELECT ID_UNIDAD_MEDICA AS id, ");
		query.append( "DES_UNIDAD_MEDICA AS 'desc' " );
		query.append( "FROM SVC_UNIDAD_MEDICA " );
		query.append( "ORDER BY ID_UNIDAD_MEDICA ASC " );
		
		return query.toString();
	}
	
	public String mesesPago() {
		
		StringBuilder query = new StringBuilder("SELECT ID_MES AS id, ");
		query.append( "DES_MES AS 'desc' " );
		query.append( "FROM SVC_MESES_PAGO " );
		query.append( "WHERE IND_ACTIVO = '1' " );
		query.append( "ORDER BY ID_MES ASC " );
		
		return query.toString();
	}
	
	public String estatusPlanSFPA() {
		
		StringBuilder query = new StringBuilder("SELECT ID_ESTATUS_PLAN_SFPA AS id, ");
		query.append( "DES_ESTATUS_PLAN_SFPA AS 'desc' " );
		query.append( "FROM SVC_ESTATUS_PLAN_SFPA " );
		query.append( "WHERE IND_ACTIVO = '1' " );
		query.append( "ORDER BY ID_ESTATUS_PLAN_SFPA ASC " );
		
		return query.toString();
	}
	
	public String validarRfcCurp(String curp, String rfc, String nss) {
		
		StringBuilder query = new StringBuilder("SELECT SP.ID_PERSONA idPersona, "
				+ "SP.NOM_PERSONA nomPersona,"
				+ " SP.NOM_PRIMER_APELLIDO materno,"
				+ " SP.NOM_SEGUNDO_APELLIDO paterno,"
				+ " SP.CVE_RFC rfc,"
				+ " SV.ID_CONTRATANTE idContratante,"
				+ " SP.CVE_CURP curp,"
				+ " SV.IND_ACTIVO estatus,"
				+ "SP.CVE_NSS nss,"
				+ "DATE_FORMAT(SP.FEC_NAC, '%d/%m/%Y') fecNacimiento, "
				+ "SP.NUM_SEXO idSexo, "
				+ "CASE "
				+ "WHEN SP.NUM_SEXO=1 THEN 'MUJER' "
				+ "WHEN SP.NUM_SEXO=2 THEN 'HOMBRE' "
				+ "ELSE 'OTRO' "
				+ "END sexo, "
				+ "SP.REF_OTRO_SEXO otroSexo, " 
				+ "IF(SP.ID_PAIS=119, 'MEXICANA', 'EXTRANJERA') nacionalidad, "
				+ "PA.DES_PAIS pais, "
				+ "SP.ID_PAIS idPais, "
				+ "EDO.DES_ESTADO lugarNac, "
				+ "SP.ID_ESTADO idLugarNac, "
				+ "SP.REF_TELEFONO tel, "
				+ "SP.REF_CORREO correo, "
				+ "DOM.REF_CALLE calle, "
				+ "DOM.NUM_EXTERIOR numExt, "
				+ "DOM.NUM_INTERIOR numInt, "
				+ "DOM.REF_CP cp, "
				+ "DOM.REF_COLONIA colonia, "
				+ "DOM.REF_MUNICIPIO municipio, "
				+ "DOM.REF_ESTADO estado, "
				+ "USR.ID_USUARIO idUsuario, "
				+ "USR.CVE_USUARIO usr"		
				+ " FROM ");
		query.append( "SVC_CONTRATANTE SV " );
		query.append( "INNER JOIN SVC_PERSONA SP ON SV.ID_PERSONA = SP.ID_PERSONA " );
		query.append( "INNER JOIN SVC_PAIS PA ON SP.ID_PAIS = PA.ID_PAIS " );
		query.append( "LEFT JOIN SVC_ESTADO EDO ON SP.ID_ESTADO = EDO.ID_ESTADO " );
		query.append( "INNER JOIN SVT_DOMICILIO DOM ON SV.ID_DOMICILIO = DOM.ID_DOMICILIO " );
		query.append( "LEFT JOIN SVT_USUARIOS USR ON USR.ID_PERSONA = SP.ID_PERSONA " );
		if(curp!=null) {
			query.append( " WHERE SP.CVE_CURP = '"+curp+"'" );
		}else if(rfc!=null){
			query.append( " WHERE SP.CVE_RFC =  '"+rfc+"'" );
		}else if(nss!=null) {
			query.append( " WHERE SP.CVE_NSS =  '"+nss+"'" );
		}
		query.append(" LIMIT 1");
		log.info("query "+query.toString());
		return query.toString();
	}
}
