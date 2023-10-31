package com.imss.sivimss.oauth.model.response;

import java.util.List;

import lombok.Data;

@Data
public class MenuResponse {

	private String idModuloPadre;
	private String idModulo;
	private String idFuncionalidad;
	private String titulo;
	private List<MenuResponse> modulos;
	
}
