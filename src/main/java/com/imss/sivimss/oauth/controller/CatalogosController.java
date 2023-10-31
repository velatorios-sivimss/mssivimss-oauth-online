package com.imss.sivimss.oauth.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.service.CatalogosService;
import com.imss.sivimss.oauth.util.ConstantsMensajes;
import com.imss.sivimss.oauth.util.Response;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/catalogos")
public class CatalogosController {
	
	@Autowired
	private CatalogosService catalogosService;
	
	@PostMapping("/consulta")
	public Response<Object> consultaListaGenerica() throws IOException {
		
		return new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				catalogosService.consulta() );

	}

}
