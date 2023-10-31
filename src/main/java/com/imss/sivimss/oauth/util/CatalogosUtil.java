package com.imss.sivimss.oauth.util;

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
}
