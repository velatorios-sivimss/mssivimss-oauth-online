package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.model.request.RenapoRequest;
import com.imss.sivimss.oauth.service.CatalogosService;
import com.imss.sivimss.oauth.util.BdConstantes;
import com.imss.sivimss.oauth.util.CatalogosUtil;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CatalogosServiceImpl extends UtileriaService implements CatalogosService {
	
	@Autowired
	private LogUtil logUtil;
	
	  @Value("${endpoints.renapo}")
	    private String urlRenapo;
	  
	  @Value("${endpoints.sepomex}")
	    private String urlSepomex;
	  
	  @Value("${endpoints.nss}")
	    private String urlNss;
	  
	  private static final String EXITO = "EXITO";

	@Override
	public Response<Object> consultaRfcCurp(String curp, String rfc, String nss) throws IOException {
	
		CatalogosUtil catalogosUtil = new CatalogosUtil();
		 RenapoRequest renapoRequest;
		String query = catalogosUtil.validarRfcCurp(curp, rfc, nss);
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		List<Map<String, Object>> datos = consultaGenericaPorQuery( query );
		if(datos==null||datos.isEmpty()) {
			if(curp!=null) {
			Map<String, Object> renapo;
			 renapo = providerRestTemplate.consumirServicioGet(urlRenapo.concat("/") +curp);
			if(renapo.get("statusOper").toString().equals("NO EXITOSO")) {
				return	generaRespuesta("NO EXISTE CURP", null);
			}
			else {
			renapoRequest =  new RenapoRequest(renapo);
			      return new Response<>(false, HttpStatus.OK.value(),EXITO,
					renapoRequest);
			}
			}else if(nss!=null){
				Response<Object> response = providerRestTemplate.consumirServicioExternoGet(urlNss.concat("/") +nss);
				if(response.getCodigo()==200){
					return new Response<>(false, HttpStatus.OK.value(),EXITO,
							response.getDatos());
				}else {
					generaRespuesta("NO EXISTE NSS", null);
				}
				
			}
			else {
				return	new Response<>(false, HttpStatus.OK.value(),EXITO,
						datos);
		}
		}
		return	generaRespuesta("USUARIO REGISTRADO", datos);
	}

	private Response<Object> generaRespuesta(String msg, List<Map<String, Object>> datos) {
		return new Response<>(true, HttpStatus.OK.value(),msg,
				datos);
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

	@Override
	public Object consultaCP(String cp) throws IOException {
		 Response<Object> response =providerRestTemplate.consumirServicioExternoGet(urlSepomex.concat("/")+cp);
		 if(response.getCodigo()==200) {
			 return new Response<>(false, HttpStatus.OK.value(),EXITO,
						response.getDatos());
		 }
		 log.info(response.getMensaje());
		 return new Response<>(true, HttpStatus.OK.value(),"NO EXISTE CP",
					null);	
	}


}
