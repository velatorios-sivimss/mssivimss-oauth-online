package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.beans.Usuario;

public interface UsuarioService {

	Usuario obtener(String user) throws IOException;
	
}
