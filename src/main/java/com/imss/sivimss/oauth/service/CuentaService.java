package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.model.Login;

public interface CuentaService {

	public Login obtenerLoginPorIdUsuario(String idUsuario) throws IOException;
	
	public Login obtenerLoginPorCveUsuario(String cveUsuario) throws IOException;
	
	public Boolean actualizarContra(String idLogin, String idUsuario, String contrasenia) throws IOException;
	
	public void validarSiap(String cveUsuario) throws IOException;
	
	public Integer actNumIntentos(String idLogin, Integer numIntentos) throws IOException;
	
	public Integer validaNumIntentos(String idLogin, String fechaBloqueo, String numIntentos) throws Exception;
	
	public Integer obtenerMaxNumIntentos() throws IOException;
}
