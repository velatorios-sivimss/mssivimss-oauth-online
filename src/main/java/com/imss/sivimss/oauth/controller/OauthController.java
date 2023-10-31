package com.imss.sivimss.oauth.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.service.MenuService;
import com.imss.sivimss.oauth.service.OauthService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class OauthController {

	@Autowired
	private OauthService oauthService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	@PostMapping("login")
	public Response<Object> acceder(@RequestBody Map<String, Object> datos) throws Exception {
		
		String user = datos.get(AppConstantes.USUARIO).toString() ;
		String contrasenia = datos.get(AppConstantes.CONTRASENIA).toString() ;
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return oauthService.acceder(user, contrasenia);
      
	}
	
	@PostMapping("menu")
	public Response<Object> menu(@RequestBody Map<String, Object> datos) throws IOException {
	
		String idRol = datos.get(AppConstantes.IDROL).toString() ;
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return menuService.obtener(idRol);
      
	}
	
	@PostMapping("mensajes")
	public Response<Object> mensajes() throws Exception {
		
		return menuService.mensajes();
      
	}
	
	@PostMapping("permisos")
	public Response<Object> permisos(@RequestBody Map<String, Object> datos) throws IOException {
	
		String idRol = datos.get(AppConstantes.IDROL).toString() ;
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return menuService.permisos(idRol);
      
	}
	
}
