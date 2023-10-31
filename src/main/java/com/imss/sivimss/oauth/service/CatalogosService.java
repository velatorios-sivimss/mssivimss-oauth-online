package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.model.response.CatalogosResponse;

public interface CatalogosService {

	public CatalogosResponse consulta() throws IOException;
	
}

