package com.imss.sivimss.oauth.model;

import java.util.List;

import lombok.Data;

@Data
public class Funcionalidad {
	
	private String idFuncionalidad;
	private List<Permisos> permisos;
	
}
