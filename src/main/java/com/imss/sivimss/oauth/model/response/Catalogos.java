package com.imss.sivimss.oauth.model.response;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Catalogos {
	
	List<Map<String, Object>> delegaciones;
	List<Map<String, Object>> nivelOficina;
	List<Map<String, Object>> parentesco;
	List<Map<String, Object>> pais;
	List<Map<String, Object>> estados;
	List<Map<String, Object>> tipoOrden;
	List<Map<String, Object>> tipoPension;
	List<Map<String, Object>> unidadesMedicas;
	List<Map<String, Object>> mesesPago;
	List<Map<String, Object>> estatusPlanSFPA;

}
