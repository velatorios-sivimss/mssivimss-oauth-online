package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.model.response.Catalogos;
import com.imss.sivimss.oauth.model.response.CatalogosResponse;
import com.imss.sivimss.oauth.service.CatalogosService;
import com.imss.sivimss.oauth.util.CatalogosUtil;
import com.imss.sivimss.oauth.util.LogUtil;

@Service
public class CatalogosServiceImpl extends UtileriaService implements CatalogosService {
	
	@Autowired
	private LogUtil logUtil;
	
	@Override
	public CatalogosResponse consulta() throws IOException {
		CatalogosUtil catalogosUtil = new CatalogosUtil();
		CatalogosResponse catalogosResponse = new CatalogosResponse();
		Catalogos catalogos = new Catalogos();
		
		String query = catalogosUtil.delegacion();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> delegaciones = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.nivelOficina();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> nivelOficina = consultaGenericaPorQuery(query  );
		
		query = catalogosUtil.parentesco();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> parentesco = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.pais();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> pais = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.estados();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> estados = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.tipoOrden();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> tipoOrden = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.tipoPension();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> tipoPension = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.unidadesMedicas();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> unidadesMedicas = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.mesesPago();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> mesesPago = consultaGenericaPorQuery( query );
		
		query = catalogosUtil.estatusPlanSFPA();
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> estatusPlanSFPA = consultaGenericaPorQuery( query );
		
		catalogos.setDelegaciones(delegaciones);
		catalogos.setNivelOficina(nivelOficina);
		catalogos.setParentesco(parentesco);
		catalogos.setPais(pais);
		catalogos.setEstados(estados);
		catalogos.setTipoOrden(tipoOrden);
		catalogos.setTipoPension(tipoPension);
		catalogos.setUnidadesMedicas(unidadesMedicas);
		catalogos.setMesesPago(mesesPago);
		catalogos.setEstatusPlanSFPA(estatusPlanSFPA);
		
		catalogosResponse.setCatalogos(catalogos);
		
		return catalogosResponse;
	}

}
