package com.imss.sivimss.oauth.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.imss.sivimss.oauth.model.request.PersonaRequest;
import com.imss.sivimss.oauth.util.Response;

public interface OauthExtService {

	Response<Object> accederExt(String user, String contrasenia) throws IOException, Exception;

	Response<Object> registrarContratante(PersonaRequest contratanteR) throws IOException, SQLException, ParseException, Exception;

}
