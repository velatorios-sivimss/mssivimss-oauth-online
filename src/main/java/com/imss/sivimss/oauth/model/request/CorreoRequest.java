package com.imss.sivimss.oauth.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CorreoRequest {

	private String nombre;
	private String codigo;
	private String correoPara;
	private String tipoCorreo;
	
}
