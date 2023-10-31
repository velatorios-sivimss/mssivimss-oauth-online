package com.imss.sivimss.oauth.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.service.ContraseniaService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/contrasenia")
public class ContraseniaController {
	
	@Autowired
	private ContraseniaService contraseniaService;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	@PostMapping("/cambiar")
	public Response<Object> acceder(@RequestBody Map<String, Object> datos) throws Exception {
		
		String user = datos.get(AppConstantes.USUARIO).toString();
		String contraseniaAnterior = datos.get(AppConstantes.CONTRASENIA_ANTERIOR).toString();
		String contraseniaNueva = datos.get(AppConstantes.CONTRASENIA_NUEVA).toString();
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return contraseniaService.cambiar(user, contraseniaAnterior, contraseniaNueva);
      
	}
	
	@PostMapping("/genera-codigo")
	public Response<Object> generarCodigo(@RequestBody Map<String, Object> datos) throws IOException {
		
		String user = datos.get(AppConstantes.USUARIO).toString();
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return contraseniaService.generarCodigo( user );
      
	}
	
	@PostMapping("/valida-codigo")
	public Response<Object> validarCodigo(@RequestBody Map<String, Object> datos) throws Exception {
		
		String user = datos.get(AppConstantes.USUARIO).toString();
		String codigo = datos.get(AppConstantes.CODIGO).toString();
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return contraseniaService.validarCodigo( user, codigo );
      
	}
	
}
