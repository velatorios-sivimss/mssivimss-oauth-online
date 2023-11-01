package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.model.response.CatalogosResponse;
import com.imss.sivimss.oauth.util.Response;

public interface CatalogosService {

	//public CatalogosResponse consulta() throws IOException;

	public Object consultaRfcCurp(String curp, String rfc) throws IOException;
	
}

