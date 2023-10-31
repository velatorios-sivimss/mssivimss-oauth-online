package com.imss.sivimss.oauth.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface VelatorioService {

	public List<Map<String, Object>> consulta(String idVelatorio) throws IOException;
	
}

