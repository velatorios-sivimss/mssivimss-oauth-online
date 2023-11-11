package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.model.Login;
import com.imss.sivimss.oauth.service.CuentaExtService;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.LoginExtUtil;
import com.imss.sivimss.oauth.util.MensajeEnum;
import com.imss.sivimss.oauth.util.ParametrosUtil;

@Service
public class CuentaExtImpl  extends UtileriaService implements CuentaExtService{

	@Autowired
	private LogUtil logUtil;
	
	private static final String TIP_PARAMETRO = "TIP_PARAMETRO";
	private static final String FEC_CAMBIO_CONTRASENIA = "FEC_CAMBIO_CONTRASENIA";
	
	@Override
	public Login obtenerLoginPorIdContratante(String idContratante) throws IOException {
		
		//AQUI SE ALMECENAN LOS DATOS DE LA CONSULTA [0] SVT_LOGIN
		List<Map<String, Object>> datos;
		LoginExtUtil loginUtil = new LoginExtUtil();
		List<Login> lista;
		//CONSULTA POR ID PARA OBTENER DEL LOGIN
		datos = consultaGenericaPorQuery( loginUtil.buscarPorIdContratante(idContratante) );
		Map<String, Object> dato;
		Login login;
		
		//SI ES NULO O VACIO SE INSERTAN LOS DATOS DEL USSUARIO EN LA TABLA LOGIN PARA ASI OBETENR SU ID
		if( datos == null || datos.isEmpty() ) {
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"",CONSULTA+" "+ "No existen datos en BD, entonces se debe crear el  registro");
			
			dato = insertarDetalle( loginUtil.insertar(idContratante) , "SVT_LOGIN", "ID_LOGIN");
			login = modelMapper.map(dato, Login.class);
		}else {
			lista = Arrays.asList(modelMapper.map(datos, Login[].class));
			login = lista.get(0);
			//DUDA QUE SE HACE?
			if( datos.get(0).get(FEC_CAMBIO_CONTRASENIA) != null) {
				login.setFecCamContra(datos.get(0).get(FEC_CAMBIO_CONTRASENIA).toString() );
			}

		}
		
		return login;
	
	}

	@Override
	public Login obtenerLoginPorCveContratante(String cveContratante) throws IOException {
		List<Map<String, Object>> datos;
		LoginExtUtil loginUtil = new LoginExtUtil();
		List<Login> lista;
		datos = consultaGenericaPorQuery( loginUtil.buscarPorCveContratante(cveContratante) );
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
	public Boolean actualizarContra(String idLogin, String idContratante, String contraNueva) throws IOException {
		Boolean exito = false;
		LoginExtUtil loginUtil = new LoginExtUtil();
		
		exito = actualizarMultiple( loginUtil.actContrasenia(idLogin, idContratante, contraNueva) );
		
		return exito;
	}
	
	@Override
	public Integer actNumIntentos(String idLogin, Integer numIntentos) throws IOException {
		LoginExtUtil loginUtil = new LoginExtUtil();
		Integer maxNumIntentos = obtenerMaxNumIntentos();
		
		actualizaGenericoPorQuery( loginUtil.actNumIntentos(idLogin, numIntentos, maxNumIntentos) );
		return maxNumIntentos;
	}

	@SuppressWarnings("unchecked")
	@Override
	//FECHA DE BLOQUEO PUEDE QUE VAYA NULO SI EL REGISTRO RECIEN SE INSERTO
	public Integer validaNumIntentos(String idLogin, String fechaBloqueo, String numIntentos) throws IOException {
		List<Map<String, Object>> datos;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		List<Map<String, Object>> mapping;
		//NULL
		Integer tiempoBloqueo;
		LoginExtUtil loginUtil = new LoginExtUtil();
		Integer intentos = Integer.parseInt(numIntentos);
		
		datos = consultaGenericaPorQuery( parametrosUtil.tiempoBloqueo() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		//SE ASIGNA EL VALOR DE 5
		tiempoBloqueo =  Integer.parseInt(mapping.get(0).get(TIP_PARAMETRO).toString());
		
		//EN CASO QUE VENGA EN NULL SOLO SE REGRESA: intentos
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
	
	
}
