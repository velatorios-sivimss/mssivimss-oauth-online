package com.imss.sivimss.oauth.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Login {
	
	@SerializedName("ID_LOGIN")
	private String idLogin;
	
	@SerializedName("ID_USUARIO")
	private String idUsuario;
	
	@SerializedName("NUM_INTENTOS")
	private String numIntentos;
	
	@SerializedName("FEC_BLOQUEO")
	private String fecBloqueo;
	
	@SerializedName("FEC_CAMBIO_CONTRASENIA")
	private String fecCamContra;
	
	@SerializedName("CVE_CODIGO_SEGURIDAD")
	private String codSeguridad;
	
	@SerializedName("FEC_CODIGO_SEGURIDAD")
	private String fecCodSeguridad;
	
	@SerializedName("CVE_ESTATUS_CUENTA")
	private String estatusCuenta;
	
}
