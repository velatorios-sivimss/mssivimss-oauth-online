package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.service.VelatorioService;
import com.imss.sivimss.oauth.util.CatalogosUtil;

@Service
public class VelatorioServiceImpl extends UtileriaService implements VelatorioService {
	
	@Override
	@Cacheable("velatorio-consulta")
	public List<Map<String, Object>> consulta(String idDelegacion) throws IOException {
		
		CatalogosUtil catalogosUtil = new CatalogosUtil();
		List<Map<String, Object>> resp;
		
		resp = consultaGenericaPorQuery( catalogosUtil.velatorios(idDelegacion) );
		
		return resp;
	}

}
