package com.imss.sivimss.oauth.service;

import java.io.IOException;
import java.text.ParseException;

import com.imss.sivimss.oauth.model.request.PersonaRequest;
import com.imss.sivimss.oauth.model.request.PlanSFPARequest;
import com.imss.sivimss.oauth.util.Response;

public interface OauthExtService {

	Response<Object> accederExt(String user, String contrasenia) throws IOException, ParseException;

	Response<Object> registrarContratante(PersonaRequest contratanteR) throws IOException;
	
}
