package com.imss.sivimss.oauth.controller;

import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.model.request.PersonaRequest;
import com.imss.sivimss.oauth.model.request.PlanSFPARequest;
import com.imss.sivimss.oauth.service.OauthExtService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class OauthExtController {
	
	@Autowired
	private OauthExtService oauthService;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	@PostMapping("login/ext")
	public Response<Object> acceder(@RequestBody Map<String, Object> datos) throws Exception {
		
		String user = datos.get(AppConstantes.USUARIO).toString() ;
		String contrasenia = datos.get(AppConstantes.CONTRASENIA).toString() ;
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return oauthService.accederExt(user, contrasenia);
      
	}
	
	@PostMapping("registrar/contratante")
	public Response<Object> registrar(@RequestBody PersonaRequest contratanteR) throws Exception {
		
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ contratanteR);
		
		return oauthService.registrarContratante(contratanteR);
      
	}
	
	@PostMapping("registrar/usuario")
	public Response<Object> registrarUsuario(@RequestBody PlanSFPARequest planSFPARequest) throws Exception {
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",AppConstantes.ALTA+" "+ planSFPARequest);
		
		return oauthService.registrarUsuario(planSFPARequest);
      
	}

}
