package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.model.response.CatalogosResponse;
import com.imss.sivimss.oauth.util.Response;

public interface CatalogosService {

	public Response<Object> consultaRfcCurp(String curp, String rfc, String nss) throws IOException;

	public Object consultaPais() throws IOException;

	public Object consultaEstado() throws IOException;

	public Object consultaCP(String cp) throws IOException;
	
}

