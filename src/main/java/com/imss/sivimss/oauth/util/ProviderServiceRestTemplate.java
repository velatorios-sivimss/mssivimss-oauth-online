package com.imss.sivimss.oauth.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import com.google.gson.Gson;
import com.imss.sivimss.oauth.security.JwtTokenProvider;


@Service
public class ProviderServiceRestTemplate {

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	@Autowired
	private JwtTokenProvider jwtTokenProvider; 
	
	private static final Logger log = LoggerFactory.getLogger(ProviderServiceRestTemplate.class);
	
	private static final String MENSAJE = "Ha ocurrido un error al recuperar la informacion";
	
	public Response<Object> consumirServicio(Object dato, String url) throws IOException {
		try {
			Response<Object> respuestaGenerado=restTemplateUtil.sendPostRequestByteArrayToken(url, dato,jwtTokenProvider.createToken(""), Response.class);
			return validarResponse(respuestaGenerado);
		} catch (IOException exception) {
			log.error( MENSAJE );
			throw exception;
		}
	}
	
	public Response<Object> consumirServicioReportes(Map<String, Object> dato, String nombreReporte, String tipoReporte, String url,Authentication authentication) throws IOException {
		try {
			Response<Object> respuestaGenerado=restTemplateUtil.sendPostRequestByteArrayReportesToken(url, new DatosReporteDTO(dato,nombreReporte, tipoReporte),jwtTokenProvider.createToken((String) authentication.getPrincipal()), Response.class);
			return validarResponse(respuestaGenerado);
		} catch (IOException exception) {
			log.error(MENSAJE);
			throw exception;
		}
	}
	
	public Response<Object>validarResponse(Response<Object> respuestaGenerado){
		String codigo = respuestaGenerado.getMensaje().substring(0, 3);
		if (codigo.equals("500") || codigo.equals("404") || codigo.equals("400") || codigo.equals("403")) {
			Gson gson = new Gson();
			String mensaje = respuestaGenerado.getMensaje().substring(7, respuestaGenerado.getMensaje().length() - 1);

			ErrorsMessageResponse apiExceptionResponse = gson.fromJson(mensaje, ErrorsMessageResponse.class);

			respuestaGenerado = Response.builder().codigo((int) apiExceptionResponse.getCodigo()).error(true)
					.mensaje(apiExceptionResponse.getMensaje()).datos(apiExceptionResponse.getDatos()).build();

		}
		return respuestaGenerado;
	}
	
	public Map<String, Object> consumirServicioGet(String url) throws IOException {
		try {
			Map<String, Object> respuestaGenerado=restTemplateUtil.sendGet(url, Map.class);
			return respuestaGenerado;
		} catch (IOException exception) {
			log.error(MENSAJE);
			throw exception;
		}
	}
}
