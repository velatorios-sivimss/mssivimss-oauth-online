package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.model.Login;
import com.imss.sivimss.oauth.service.CuentaService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.LoginUtil;
import com.imss.sivimss.oauth.util.MensajeEnum;
import com.imss.sivimss.oauth.util.ParametrosUtil;

@Service
public class CuentaServiceImpl extends UtileriaService implements CuentaService {

	@Value("${endpoints.consulta-siap}")
	private String urlConsultaSiap;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String TIP_PARAMETRO = "TIP_PARAMETRO";
	private static final String FEC_CAMBIO_CONTRASENIA = "FEC_CAMBIO_CONTRASENIA";
	
	@Override
	public Login obtenerLoginPorIdUsuario(String idUsuario) throws IOException {
		
		List<Map<String, Object>> datos;
		LoginUtil loginUtil = new LoginUtil();
		List<Login> lista;
		datos = consultaGenericaPorQuery( loginUtil.buscarPorIdUsuario(idUsuario) );
		Map<String, Object> dato;
		Login login;
		
		if( datos == null || datos.isEmpty() ) {
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"",CONSULTA+" "+ "No existen datos en BD, entonces se debe crear el  registro");
			
			dato = insertarDetalle( loginUtil.insertar(idUsuario) , "SVT_LOGIN", "ID_LOGIN");
			login = modelMapper.map(dato, Login.class);
		}else {
			lista = Arrays.asList(modelMapper.map(datos, Login[].class));
			login = lista.get(0);
			
			if( datos.get(0).get(FEC_CAMBIO_CONTRASENIA) != null) {
				login.setFecCamContra( datos.get(0).get(FEC_CAMBIO_CONTRASENIA).toString() );
			}

		}
		
		return login;
	
	}

	@Override
	public Login obtenerLoginPorCveUsuario(String cveUsuario) throws IOException {
		List<Map<String, Object>> datos;
		LoginUtil loginUtil = new LoginUtil();
		List<Login> lista;
		datos = consultaGenericaPorQuery( loginUtil.buscarPorCveUsuario(cveUsuario) );
		Login login;
		
		if( datos == null || datos.isEmpty() ) {
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"",CONSULTA+" "+ "No existen datos en BD");
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Usuario no Existe");
		}else {
			lista = Arrays.asList(modelMapper.map(datos, Login[].class));
			login = lista.get(0);
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Datos del Login " + datos);
			
			if( datos.get(0).get(FEC_CAMBIO_CONTRASENIA) != null ) {
				login.setFecCamContra( datos.get(0).get(FEC_CAMBIO_CONTRASENIA).toString() );
			}
			
			if( datos.get(0).get("CVE_CODIGO_SEGURIDAD") != null ) {
				login.setCodSeguridad( datos.get(0).get("CVE_CODIGO_SEGURIDAD").toString() );
			}
			
			if( datos.get(0).get("FEC_CODIGO_SEGURIDAD") != null ) {
				login.setFecCodSeguridad( datos.get(0).get("FEC_CODIGO_SEGURIDAD").toString() );
			}
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Objeto Login Mapeado" + login);
			
		}
		
		return login;
	}

	@Override
	public Boolean actualizarContra(String idLogin, String idUsuario, String contrasenia) throws IOException {
		
		Boolean exito = false;
		LoginUtil loginUtil = new LoginUtil();
		
		exito = actualizarMultiple( loginUtil.actContrasenia(idLogin, idUsuario, contrasenia) );
		
		return exito;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validarSiap(String cveUsuario) throws IOException {
		List<Map<String, Object>> datos;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		List<Map<String, Object>> mapping;
		String estatusSiap = "";
		
		if(cveUsuario == null || cveUsuario.isEmpty()) {
			return; 
		}
		
		datos = consultaGenericaPorQuery( parametrosUtil.consultarSiap() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		String siap = mapping.get(0).get(TIP_PARAMETRO).toString();
		
		if( siap.equalsIgnoreCase("true") ) {
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"",CONSULTA+" "+
			"Se debe consultar el SIAP");
			estatusSiap = consultaSiap(cveUsuario);
		}
		
		if( siap.equalsIgnoreCase("true") && !estatusSiap.equalsIgnoreCase(AppConstantes.SIAP_ACTIVO) ) {
			throw new BadRequestException(HttpStatus.OK, MensajeEnum.SIAP_DESACTIVADO.getValor());
		}
		
	}

	@Override
	public Integer actNumIntentos(String idLogin, Integer numIntentos) throws IOException {
		LoginUtil loginUtil = new LoginUtil();
		Integer maxNumIntentos = obtenerMaxNumIntentos();
		
		actualizaGenericoPorQuery( loginUtil.actNumIntentos(idLogin, numIntentos, maxNumIntentos) );
		return maxNumIntentos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer validaNumIntentos(String idLogin, String fechaBloqueo, String numIntentos) throws Exception {
		List<Map<String, Object>> datos;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		List<Map<String, Object>> mapping;
		Integer tiempoBloqueo;
		LoginUtil loginUtil = new LoginUtil();
		Integer intentos = Integer.parseInt(numIntentos);
		
		datos = consultaGenericaPorQuery( parametrosUtil.tiempoBloqueo() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		tiempoBloqueo =  Integer.parseInt(mapping.get(0).get(TIP_PARAMETRO).toString());
		
		if( fechaBloqueo!=null && !fechaBloqueo.isEmpty() ) {
			
			datos = consultaGenericaPorQuery( loginUtil.difTiempoBloqueo( idLogin ) );
			mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Datos Fecha Bloqueo "+ mapping);
			
			Integer diferencia = Integer.parseInt(mapping.get(0).get("diferencia").toString());
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Diferencia de Tiempo "+ diferencia);
			
			if( diferencia > tiempoBloqueo ) {
				//resetear numBloqueo
				intentos = 0;
				actualizaGenericoPorQuery( loginUtil.actNumIntentos(idLogin, 0, 1) );
			}else {
				throw new BadRequestException(HttpStatus.BAD_REQUEST, MensajeEnum.INTENTOS_FALLIDOS.getValor());
			}

		}
		
		return intentos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer obtenerMaxNumIntentos()  throws IOException{
		Integer maxNumIntentos;
		List<Map<String, Object>> datos;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		List<Map<String, Object>> mapping;
		
		datos = consultaGenericaPorQuery( parametrosUtil.numIntentos() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		maxNumIntentos = Integer.parseInt(mapping.get(0).get(TIP_PARAMETRO).toString());
		
		return maxNumIntentos;
	}
	
	private String consultaSiap(String matricula) throws IOException {
		String status;
		Map<String, Object> resp;
		String url = urlConsultaSiap + matricula;
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(),"",CONSULTA+" "+ url);
		
		//Hacemos el consumo para consultar el SIAP
		resp = providerRestTemplate.consumirServicioGet(url);
		status = (String) resp.get("status");
		
		return status;
	}

}
