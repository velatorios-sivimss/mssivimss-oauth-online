package com.imss.sivimss.oauth.util;

import lombok.Getter;

@Getter
public enum MensajeEnum {
	OK("INICIO_SESION_CORRECTO", "Respuesta Exitosa"),
	CONTRASENIA_PROXIMA_VENCER("CONTRASENIA_PROXIMA_VENCER", "La fecha del último cambio de contraseña está a quince días de caducar"),
	CONTRASENIA_INCORRECTA("CREDENCIALES_INCORRECTAS", "La contraseña ingresada es incorrecta"),
	INTENTOS_FALLIDOS("CANTIDAD_MAX_INTENTOS_FALLIDOS", "Se ha alcanzado el maximo de intentos fallidos, favor de esperar "),
	CONTRASENIA_VENCIDA("FECHA_CONTRASENIA_VENCIDA", "La fecha del último cambio de contraseña es superior a 3 meses"),
	USUARIO_PREACTIVO("USUARIO_PREACTIVO", "El usuario es Pre Activo"),
	ESTATUS_DESACTIVADO("CUENTA_BLOQUEADA", "El usuario ha sido Desactivado"),
	CODIGO_CORRECTO("CODIGO_CORRECTO", "El Codigo ingresado es Correcto"),
	CODIGO_INCORRECTO("CODIGO_INCORRECTO", "El Codigo ingresado es Incorrecto"),
	CODIGO_EXPIRADO("CODIGO_EXPIRADO", "El Codigo ingresado esta expirado"),
	SIAP_DESACTIVADO("SIAP_DESACTIVADO", "El usuario esta Desactivado en el SIAP"),
	SIAP_SIN_CONEXION("SIAP_SIN_CONEXION", "No hay conexion con el SIAP"),
	NO_EXISTE_USUARIO("NO_EXISTE_USUARIO", "Usuario no existe");
	
	String valor;
	String desc;
	
	private MensajeEnum(String valor, String desc) {
		this.valor = valor;
		this.desc = desc;
	}
	
}
