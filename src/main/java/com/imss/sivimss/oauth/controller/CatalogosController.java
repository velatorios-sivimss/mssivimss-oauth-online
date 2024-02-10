package com.imss.sivimss.oauth.controller;

import java.io.IOException;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.oauth.service.CatalogosService;
import com.imss.sivimss.oauth.util.ConstantsMensajes;
import com.imss.sivimss.oauth.util.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/catalogos")
public class CatalogosController {
	
	@Autowired
	private CatalogosService catalogosService;
	
	@PostMapping("/consulta/rfc-curp")
	public Response<Object> consultaRfcCurp(@RequestBody Map<String, Object> datos) throws IOException {
		Response<Object>response;
		String curp=null;
		String rfc=null;
		String nss=null;
		if(datos.get("curp")!=null) {
			curp = datos.get("curp").toString();
		}else if(datos.get("rfc")!=null){
			rfc = datos.get("rfc").toString();
		}else if(datos.get("nss")!=null) {
			nss = datos.get("nss").toString();
		}
		 response = catalogosService.consultaRfcCurp(curp, rfc, nss);
	/*	 if(response.getDatos().toString().equals("[]")) {
			 response = new Response<>(false, HttpStatus.OK.value(),"45",
						response.getDatos());
		 }else if(response.getMensaje().equals("USUARIO REGISTRADO")) {
			 response = new Response<>(false, HttpStatus.OK.value(),"USUARIO REGISTRADO",
						response.getDatos());
		 } */
		return response;

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
