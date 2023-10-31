package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.util.Response;

public interface MenuService {

	Response<Object> obtener(String idRol) throws IOException;
	
	Response<Object> mensajes() throws IOException;
	
	Response<Object> permisos(String idRol) throws IOException;
	
}
