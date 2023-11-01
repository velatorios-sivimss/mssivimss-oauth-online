package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.model.Login;

public interface CuentaExtService {
	
	public Login obtenerLoginPorIdContratante(String idUsuario) throws IOException;

	public Login obtenerLoginPorCveContratante(String contratante) throws IOException;

	public Boolean actualizarContra(String idLogin, String idContratante, String contraNueva) throws IOException;
	
	public Integer actNumIntentos(String idLogin, Integer numIntentos) throws IOException;
	
	public Integer validaNumIntentos(String idLogin, String fechaBloqueo, String numIntentos) throws Exception;
	
	public Integer obtenerMaxNumIntentos() throws IOException;

}
