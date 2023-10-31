package com.imss.sivimss.oauth.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DomicilioDto {

	
	private String calle;
	private String numInt;
	private String numExt;
	private Integer cp;
	private String estado;
	private String municipio;
	private String colonia;
	
	
}
