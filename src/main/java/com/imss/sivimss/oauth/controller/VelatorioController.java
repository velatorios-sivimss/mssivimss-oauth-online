package com.imss.sivimss.oauth.controller;

import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.service.VelatorioService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.ConstantsMensajes;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/velatorio")
public class VelatorioController {

	@Autowired
	private VelatorioService velatorioService;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	@PostMapping("/consulta")
	public Response<Object> consultaListaGenerica( @RequestBody Map<String, Object> datos ) throws Exception {
		
		String idDelegacion = null;
		
		if(datos.get(AppConstantes.ID_DELEGACION) != null ) {
			idDelegacion = datos.get(AppConstantes.ID_DELEGACION).toString();
		}
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ datos);
		
		return new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				velatorioService.consulta( idDelegacion ) );

	}

}
