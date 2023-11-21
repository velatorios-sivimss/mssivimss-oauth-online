package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.beans.Contratante;
import com.imss.sivimss.oauth.service.CatalogosService;
import com.imss.sivimss.oauth.util.BdConstantes;
import com.imss.sivimss.oauth.util.CatalogosUtil;
import com.imss.sivimss.oauth.util.LogUtil;

@Service
public class CatalogosServiceImpl extends UtileriaService implements CatalogosService {
	
	@Autowired
	private LogUtil logUtil;
	

	@SuppressWarnings("unchecked")
	@Override
	public Object consultaRfcCurp(String curp, String rfc) throws IOException {
		CatalogosUtil catalogosUtil = new CatalogosUtil();
		List<Map<String, Object>> lista;
		Contratante contratante;
		String query = catalogosUtil.validarRfcCurp(curp, rfc);
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> datos = consultaGenericaPorQuery( query );
		if(datos==null||datos.isEmpty()) {
			return datos;
		}
		lista = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		contratante =  new Contratante(lista.get(0));
		return contratante;
	}

	@Override
	public Object consultaPais() throws IOException {
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+  BdConstantes.PAIS);
		return consultaGenericaPorQuery(  BdConstantes.PAIS );
	}
	
	@Override
	public Object consultaEstado() throws IOException {
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+  BdConstantes.EDO);
		return consultaGenericaPorQuery(  BdConstantes.EDO );
	}

}
