package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.util.Response;

public interface ContraseniaService {

	public Response<Object> cambiar(String user, String contraAnterior, String contraNueva) throws Exception;
	public Integer validarFecha(String fecha) throws Exception;
	public Response<Object> generarCodigo(String user) throws IOException;
	public Response<Object> validarCodigo(String user, String codigo) throws Exception;
	
}
