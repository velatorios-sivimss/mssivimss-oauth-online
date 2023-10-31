package com.imss.sivimss.oauth.util;

import lombok.Getter;

@Getter
public enum EstatusVigenciaEnum {
	VALIDA(0, "Fecha Valida"),
	PROXIMA_VENCER(1, "La fecha del último cambio de contraseña está a quince días de caducar"),
	VENCIDA(2, "La fecha del último cambio de contraseña es superior a 3 meses");
	
	Integer id;
	String desc;
	
	private EstatusVigenciaEnum(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
}
