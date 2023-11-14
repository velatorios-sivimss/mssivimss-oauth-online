package com.imss.sivimss.oauth.service;

import java.io.IOException;
import java.text.ParseException;

import com.imss.sivimss.oauth.util.Response;

public interface ContraseniaExtService {

	Response<Object> cambiar(String user, String contraseniaAnterior, String contraseniaNueva) throws Exception;

	Response<Object> generarCodigo(String user) throws IOException;

	Response<Object> validarCodigo(String user, String codigo) throws IOException;
	
	public Integer validarFecha(String fecha) throws IOException, ParseException;

}
