package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.beans.Contratante;
import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.service.ContratanteService;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.MensajeEnum;

@Service
public class ContratanteImpl extends UtileriaService implements ContratanteService {
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String CONSULTA = "consulta";
	
	@SuppressWarnings("unchecked")
	@Override
	public Contratante obtener(String contratante) throws IOException {
		Contratante usuario= new Contratante();
		List<Map<String, Object>> mapping;
		//se manda a metodo buscar usuario
	    String query = usuario.buscarContratante(contratante);
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ query);
		
		//MANDA A EJECUTAR LA CONSULTAR
		List<Map<String, Object>> datos = consultaGenericaPorQuery( query );
		
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		if( (mapping == null) || (mapping.isEmpty()) ) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, MensajeEnum.NO_EXISTE_USUARIO.getValor());
		}else {
			usuario= new Contratante( mapping.get(0) );
		}
		return usuario;
	}
	}

