package com.imss.sivimss.oauth.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.model.request.PersonaRequest;
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
	
	@PostMapping("/consulta/rfc-curp")
	public Response<Object> consultaRfcCurp(@RequestBody PersonaRequest datos) throws IOException {
		 return catalogosService.consultaRfcCurp(datos);
	}

	@GetMapping("/consulta/pais")
	public Response<Object> consultaPais() throws IOException {
		 return new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				catalogosService.consultaPais());

	}
	
	@GetMapping("/consulta/estado")
	public Response<Object> consultaEstado() throws IOException {
		 return new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				catalogosService.consultaEstado());

	}
	
	@GetMapping("/consulta/cp/{cp}")
	public Object consultaCp(@PathVariable("cp") String cp) throws IOException {
		 return catalogosService.consultaCP(cp);

	}

	
}
