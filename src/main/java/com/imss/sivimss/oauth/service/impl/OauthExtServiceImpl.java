package com.imss.sivimss.oauth.service.impl;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imss.sivimss.oauth.beans.Contratante;
import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.model.Login;
import com.imss.sivimss.oauth.model.request.PersonaRequest;
import com.imss.sivimss.oauth.service.ContraseniaExtService;
import com.imss.sivimss.oauth.service.ContratanteService;
import com.imss.sivimss.oauth.service.CuentaExtService;
import com.imss.sivimss.oauth.service.OauthExtService;
import com.imss.sivimss.oauth.util.BdConstantes;
import com.imss.sivimss.oauth.util.Database;
import com.imss.sivimss.oauth.util.EstatusVigenciaEnum;
import com.imss.sivimss.oauth.util.GeneraCredencialesUtil;
import com.imss.sivimss.oauth.util.MensajeEnum;
import com.imss.sivimss.oauth.util.ParametrosUtil;
import com.imss.sivimss.oauth.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OauthExtServiceImpl extends UtileriaService implements OauthExtService {
	
	@Autowired
	GeneraCredencialesUtil generaCredenciales;
	
	@Autowired
	private ContratanteService contratanteService;
	
	@Autowired
	private CuentaExtService cuentaService;
	
	@Autowired
	private ContraseniaExtService contraseniaService;
	
	 @Autowired
	 private Database database;
	
	 private ResultSet rs;
	 
	private Statement statement;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> accederExt(String user, String contrasenia) throws IOException, ParseException {
		
		List<Map<String, Object>> mapping;
		//se detiene a obtener los datos del usuario
		Contratante usuario= contratanteService.obtener(user);
		//AQUI YA TIENE TODOS LOS DATOS DEL USUARIO
		List<Map<String, Object>> datos;
		Response<Object> resp;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		Map<String, Object> respuesta = new HashMap<>();
		String mensaje = null;
		
		if( usuario.getActivo().equals("false") ) {
			resp =  new Response<>(false, HttpStatus.CREATED.value(), MensajeEnum.ESTATUS_DESACTIVADO.getValor(),
					null );
			return resp;
		}
		
		//AQUI OBTIENE LOS DATOS DEL LOGIN DEL USR
		Login login = cuentaService.obtenerLoginPorIdContratante( usuario.getIdUsuario() );
		//FEC_BLOQUEO PUEDE QUE VAYA NULO
		Integer intentos = cuentaService.validaNumIntentos(login.getIdLogin(), login.getFecBloqueo(), login.getNumIntentos());
		
		//SI LA CONTRASENIA ES DIFERENTE ENTRA ESTA VALIDACION
		if ( !passwordEncoder.matches( contrasenia, usuario.getPassword() ) && !contrasenia.equals( usuario.getPassword() ) ) {
			intentos++;
			Integer maxNumIntentos = cuentaService.actNumIntentos(login.getIdLogin(), intentos);
			
			if( intentos >= maxNumIntentos ) {
				mensaje =  MensajeEnum.INTENTOS_FALLIDOS.getValor();
			}else {
				mensaje = MensajeEnum.CONTRASENIA_INCORRECTA.getValor();
			}
			
			throw new BadRequestException(HttpStatus.BAD_REQUEST, mensaje);
			
		}else {
			cuentaService.actNumIntentos(login.getIdLogin(), 0);
		}
	//SI EL ESTATUS DE LA CUENTA ES PREACTIVO ENTRA AL METODO NOTA: PREGUNTAR PORQUE NO MEJOR SE USO EL CAMPO BOOLEAN
		if( login.getEstatusCuenta().equalsIgnoreCase( BdConstantes.ESTATUS_PRE_ACTIVO ) ) {
			
			resp =  new Response<>(false, HttpStatus.OK.value(), MensajeEnum.USUARIO_PREACTIVO.getValor(),
					null );
			
			return resp;
			//SI ESTA DESCATIVADA LA CUENTA REGRESA ESTE MENSAJE "DESACTIVADA"
		}else if ( login.getEstatusCuenta().equalsIgnoreCase( BdConstantes.ESTATUS_DESACTIVADO ) ) {
			
			resp =  new Response<>(false, HttpStatus.OK.value(), MensajeEnum.ESTATUS_DESACTIVADO.getValor(),
					respuesta );
			
			return resp;
			
		}
		
		//Validar Fecha de la Contrasenia
		Integer estatusContra = contraseniaService.validarFecha( login.getFecCamContra() );
		
		if( estatusContra.equals( EstatusVigenciaEnum.VENCIDA.getId() ) ) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, MensajeEnum.CONTRASENIA_VENCIDA.getValor());
		}else if ( estatusContra.equals( EstatusVigenciaEnum.PROXIMA_VENCER.getId() ) ) {
			mensaje = MensajeEnum.CONTRASENIA_PROXIMA_VENCER.getValor();
		}else {
			mensaje = MensajeEnum.OK.getValor();
		}
		
		Map<String, Object> mapa = new HashMap<>();
		mapa.put("nombre", usuario.getNombre() + " " + usuario.getPaterno() + " " + usuario.getMaterno());
		mapa.put("curp", usuario.getCurp());
		mapa.put("idPais",  usuario.getIdPais()!=null ? usuario.getIdPais() : "119"  );
		mapa.put("idEstado", usuario.getIdEstado());	
		mapa.put("cveMatricula", usuario.getClaveMatricula());
		mapa.put("cveUsuario", usuario.getClaveUsuario());
		mapa.put("idUsuario", usuario.getIdUsuario());
		mapa.put("idRol", usuario.getIdRol());
		mapa.put("desRol", usuario.getRol());
		mapa.put("idContratante", usuario.getIdContratante());
		mapa.put("idPersona", usuario.getIdPersona());
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(mapa);
		log.info("token ->"+json);
		
		datos = consultaGenericaPorQuery( parametrosUtil.tiempoToken() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		String tiempoString = mapping.get(0).get("TIP_PARAMETRO").toString();
		Long tiempo = (long) Integer.parseInt(tiempoString);
		
		String token = jwtProvider.createToken(json, tiempo);
		
		resp =  new Response<>(false, HttpStatus.OK.value(), mensaje,
				token );
		
		return resp;
		
	}
	
	@Override
	public Response<Object> registrarContratante(PersonaRequest contratanteR) throws IOException {
		Response <Object> resp;
		Contratante contratante = new Contratante();
		Integer idContratante = contratanteR.getContratante().getIdContratante();
	     List<Map<String, Object>> datos;
			datos = consultaGenericaPorQuery(contratante.validarPorNss(contratanteR.getNss()));
		
		if(contratanteR.getIdUsuario()!=null || !datos.isEmpty()) {
			return new Response<>(true, HttpStatus.OK.value(), "197",
					null );
		}
		
		Connection connection = database.getConnection();
		
		try {
			
			statement = connection.createStatement();
			connection.setAutoCommit(false);
			if(contratanteR.getContratante().getIdContratante()==null) {
			statement.executeUpdate(contratante.insertarPersona(contratanteR),
						Statement.RETURN_GENERATED_KEYS);
			 rs = statement.getGeneratedKeys();
					  if (rs.next()) {
							contratanteR.getContratante().setIdPersona(rs.getInt(1));
					  }
				statement.executeUpdate(
						contratante.insertarDomicilio(contratanteR.getDomicilio()),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					contratanteR.getContratante().setIdDomicilio(rs.getInt(1));
				}     
				statement.executeUpdate(contratante.insertarContratante(contratanteR.getContratante()),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					idContratante= rs.getInt(1);
					log.info(idContratante.toString());  
				}
			}
				String contrasenia= generaCredenciales.generarContrasenia(contratanteR.getNombre() , contratanteR.getPaterno());
				String user = generaCredenciales.insertarUser(idContratante,contratanteR.getNombre(), contratanteR.getPaterno(), contrasenia, contratanteR.getContratante().getIdPersona(), statement);
				
				if(contratanteR.getCorreo()!=null) {
					  resp = generaCredenciales.enviarCorreo(user, contratanteR.getCorreo(), contratanteR.getNombre(), contratanteR.getPaterno(), contratanteR.getMaterno(), contrasenia);
					 log.info("CORREO ENVIADO CORRECTAMENTE "+idContratante );
					 resp = new Response<>(false, HttpStatus.OK.value(), "OK",
								idContratante );
				}else {
					  resp = new Response<>(false, HttpStatus.OK.value(), "ERROR AL ENVIAR CORREO ELECTRONICO",
								idContratante );
					  log.info("USUARIO SIN CORREO ELECTRONICO "+idContratante );
				}
					connection.commit();
		}catch (Exception e) {
			throw new IOException("Fallo al ejecutar la query" + e.getMessage());
		
		}finally {
			
			try {
				
				if(statement!=null) {
					statement.close();
				}                               
				if(connection!=null) {
					connection.close();
				}
				
			} catch (SQLException ex) {
				
				log.info(ex.getMessage());
    
			}
	}
		
			return resp;
	}

}
	

