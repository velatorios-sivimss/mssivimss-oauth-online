package com.imss.sivimss.oauth.model.request;

import java.util.Map;

import com.imss.sivimss.oauth.beans.Contratante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RenapoRequest {
	
	private String apellido1;
	private String apellido2;
	private String nombre;
	private String sexo;
	private String fechNac;
	private String desEntidadNac;
	private String nacionalidad;
	
	
	public RenapoRequest(Map<String, Object> renapo) {
		this.apellido1 = renapo.get("apellido1").toString();
		this.apellido2 = renapo.get("apellido2").toString();
		this.nombre = renapo.get("nombre").toString();
		this.sexo = renapo.get("sexo").toString();
		this.fechNac = renapo.get("fechNac").toString().replace("/", "-");
		this.desEntidadNac = renapo.get("desEntidadNac").toString();
		this.nacionalidad = renapo.get("nacionalidad").toString();
	}
	
		
}
