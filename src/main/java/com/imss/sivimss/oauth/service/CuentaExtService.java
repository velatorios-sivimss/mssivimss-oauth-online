package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.model.Login;

public interface CuentaExtService {
	
	public Login obtenerLoginPorIdContratante(String idUsuario) throws IOException;

	public Login obtenerLoginPorCveContratante(String contratante) throws IOException;

	public Boolean actualizarContra(String idLogin, String idContratante, String contraNueva) throws IOException;

}
