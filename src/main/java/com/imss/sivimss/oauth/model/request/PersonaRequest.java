package com.imss.sivimss.oauth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.imss.sivimss.oauth.model.ContratanteDto;
import com.imss.sivimss.oauth.model.DomicilioDto;

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
@JsonIgnoreType(value = true)
public class PersonaRequest {
	
	private String curp;
	private String nss;
	private String nombre;
	private String paterno;
	private String materno;
	private String rfc;
	private Integer numSexo;
	private String otroSexo;
	private String fecNacimiento;
	private Integer idPais;
	private Integer idLugarNac;
	private String tel;
	private String telFijo;
	private String correo;
	private ContratanteDto contratante;
	private DomicilioDto domicilio;

}
