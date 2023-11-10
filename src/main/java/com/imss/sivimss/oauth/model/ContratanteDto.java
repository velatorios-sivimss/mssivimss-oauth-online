package com.imss.sivimss.oauth.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ContratanteDto {
	
	private Integer idContratante;
	private Integer idPersona;
	private Integer idDomicilio;
	private String matricula;
	private Integer estatus;

}
