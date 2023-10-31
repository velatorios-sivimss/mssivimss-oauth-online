package com.imss.sivimss.oauth.service;

import com.imss.sivimss.oauth.util.Response;

public interface OauthService {

	Response<Object> acceder(String user, String contrasenia) throws Exception;
	
}
