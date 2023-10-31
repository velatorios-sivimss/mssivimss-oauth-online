package com.imss.sivimss.oauth.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "DatosUsuario")
public class DatosUsuarioDTO {
	@JsonProperty
	public String rol;

	@JsonProperty
	public String matricula;
	
	@JsonProperty
	public Integer IDOOAD;
	
	@JsonProperty
	public String CVEDEPARTAMENTO;
}
