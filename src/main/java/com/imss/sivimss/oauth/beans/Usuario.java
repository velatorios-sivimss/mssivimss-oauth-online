package com.imss.sivimss.oauth.beans;

import java.util.Map;

import com.imss.sivimss.oauth.util.BdConstantes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Usuario {
	private String idUsuario;
	private String materno;
	private String nombre;
	private String correo;
	private String claveMatricula;
	private String password;
	private String paterno;
	private String statusCuenta;
	private String idOficina;
	private String idVelatorio;
	private String idRol;
	private String desRol;
	private String idDelegacion;
	private String curp;
	private String claveUsuario;
	private String activo;
	
	public Usuario(Map<String, Object> datos) {
		this.idUsuario = datos.get("ID_USUARIO").toString();
		this.nombre = datos.get("NOM_USUARIO").toString();
		this.paterno = datos.get("NOM_APELLIDO_PATERNO").toString();
		this.materno = datos.get("NOM_APELLIDO_MATERNO").toString();
		this.correo = datos.get("REF_CORREOE").toString();
		this.password = datos.get("CVE_CONTRASENIA").toString();
		
		if( datos.get("CVE_MATRICULA") != null ) {
			this.claveMatricula = datos.get("CVE_MATRICULA").toString();
		}
		
		this.idOficina = datos.get("ID_OFICINA").toString();
		
		if( datos.get("ID_DELEGACION") != null ) {
			this.idDelegacion = datos.get("ID_DELEGACION").toString();
		}
		
		if( datos.get("ID_VELATORIO") != null ) {
			this.idVelatorio = datos.get("ID_VELATORIO").toString();
		}
		
		this.idRol = datos.get("ID_ROL").toString();
		this.desRol = datos.get("DES_ROL").toString();
		this.curp = datos.get("CVE_CURP").toString();
		this.claveUsuario = datos.get("CVE_USUARIO").toString();
		this.activo = datos.get("IND_ACTIVO").toString();
	}
	
	
	public String buscarUsuario(String user) {
		
		StringBuilder query = new StringBuilder(BdConstantes.SELECT_USUARIOS);
		query.append( "INNER JOIN SVC_ROL ROL ON ROL.ID_ROL = US.ID_ROL " );
		query.append( BdConstantes.WHERE );
		query.append( BdConstantes.CVE_USUARIO + " = ");
		query.append( "'" + user + "' " );
		query.append( BdConstantes.LIMIT );
		
		return query.toString();
	}
	
}
