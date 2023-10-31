package com.imss.sivimss.oauth.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CatalogosResponse {

	@JsonProperty
	private Catalogos catalogos;
}
