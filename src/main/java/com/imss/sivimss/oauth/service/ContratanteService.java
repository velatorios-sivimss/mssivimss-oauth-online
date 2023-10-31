package com.imss.sivimss.oauth.service;

import java.io.IOException;

import com.imss.sivimss.oauth.beans.Contratante;

public interface ContratanteService {
	
	Contratante obtener(String contratante) throws IOException;
}
