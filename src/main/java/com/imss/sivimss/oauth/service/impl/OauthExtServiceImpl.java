package com.imss.sivimss.oauth.service.impl;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imss.sivimss.oauth.beans.Contratante;
import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.model.Login;
import com.imss.sivimss.oauth.model.request.CorreoRequest;
import com.imss.sivimss.oauth.model.request.PersonaRequest;
import com.imss.sivimss.oauth.service.ContraseniaExtService;
import com.imss.sivimss.oauth.service.ContratanteService;
import com.imss.sivimss.oauth.service.CuentaExtService;
import com.imss.sivimss.oauth.service.OauthExtService;
import com.imss.sivimss.oauth.util.AppConstantes;
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
	
	
	@Value("${endpoints.envio-correo}")
	private String urlEnvioCorreo;
	
	@Autowired
	private ContratanteService contratanteService;
	
	@Autowired
	private CuentaExtService cuentaService;
	
	@Autowired
	private ContraseniaExtService contraseniaService;
	
	 @Autowired
	 private Database database;
	
	private Statement statement;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> accederExt(String user, String contrasenia) throws Exception {
		
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
		Login login = cuentaService.obtenerLoginPorIdContratante( usuario.getIdContratante() );
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
		
		//NO FUNCIONAL PARA CU43
		//Validacion del SIAP
		//cuentaService.validarSiap( usuario.getClaveMatricula() );
		
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
		mapa.put("idPais", usuario.getIdPais() );
		mapa.put("idEstado", usuario.getIdEstado());
		mapa.put("cveMatricula", usuario.getClaveMatricula());
		mapa.put("cveUsuario", usuario.getClaveUsuario());
		mapa.put("idUsuario", usuario.getIdContratante());
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(mapa);
		log.info("token ->"+json);
		
		datos = consultaGenericaPorQuery( parametrosUtil.tiempoToken() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		String tiempoString = mapping.get(0).get("TIP_PARAMETRO").toString();
		//
		Long tiempo = (long) Integer.parseInt(tiempoString);
		
		String token = jwtProvider.createToken(json, tiempo);
		
		resp =  new Response<>(false, HttpStatus.OK.value(), mensaje,
				token );
		
		return resp;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> registrarContratante(PersonaRequest contratanteR) throws Exception {
		GeneraCredencialesUtil generaCredenciales = new GeneraCredencialesUtil();  
		Connection connection = database.getConnection();
	        connection.setAutoCommit(false);
		Contratante contratante = new Contratante();
		Integer id = 0;
		
		try {
			ResultSet rs;
			if(contratanteR.getContratante().getIdContratante()==null) {
				
				String contrasenia= generaCredenciales.generarContrasenia(contratanteR.getNombre() , contratanteR.getPaterno());
			//String contrasenia= generarContrasenia(contratanteR.getNombre(), contratanteR.getPaterno());
			statement = connection.createStatement();
			List<Map<String, Object>> mapping;
			String query = contratante.cuentaUsuarios();
			List<Map<String, Object>> datos = consultaGenericaPorQuery( query );
			mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
			Integer numberUser = Integer.parseInt(mapping.get(0).get("max").toString());
			String user = generaCredenciales.obtenerUser(numberUser,contratanteR.getNombre(), contratanteR.getPaterno());
			//String user =obtenerUser(numberUser,contratanteR.getNombre(), contratanteR.getPaterno());
				statement.executeUpdate(contratante.insertarPersona(contratanteR),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					contratanteR.getContratante().setIdPersona(rs.getInt(1));
				}
				statement.executeUpdate(
						contratante.insertarDomicilio(contratanteR.getDomicilio(), contratanteR.getContratante().getIdDomicilio()),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					contratanteR.getContratante().setIdDomicilio(rs.getInt(1));
				}
				String hash = passwordEncoder.encode(contrasenia);      
				statement.executeUpdate(contratante.insertarContratante(contratanteR.getContratante(), hash, user),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					id= rs.getInt(1);
					log.info(id.toString());
					String credenciales = "<b>Usuario:</b> "+user+"<br> <b>Pass: </b>"+contrasenia;
					 CorreoRequest correo = new CorreoRequest(contratanteR.getNombre(), credenciales, contratanteR.getCorreo(), AppConstantes.USR_CONTRASENIA);
						//Hacemos el consumo para enviar el codigo por correo
						Response <Object> resp = providerRestTemplate.consumirServicio(correo, urlEnvioCorreo);
						
						if (resp.getCodigo() != 200) {
							return resp;
						}
					
				}
			}else {
				statement.executeUpdate(contratante.insertarPersona(contratanteR),
						Statement.RETURN_GENERATED_KEYS);
				statement.executeUpdate(
						contratante.insertarDomicilio(contratanteR.getDomicilio(), contratanteR.getContratante().getIdDomicilio()),
						Statement.RETURN_GENERATED_KEYS);
			}
		}catch (Exception e) {
            log.info(e.getMessage());
            statement.close();
            connection.rollback();
            connection.close();
            log.info("FALLO AL EJECUTAR LA QUERY" + e.getMessage());
            throw new IOException("5");
		}finally {
			 try{
	                statement.close();
	                connection.commit();
	                connection.close();
	            }catch (SQLException se){
	                log.info(se.getMessage());
	            }
		}
     
	 
		return  new Response<>(false, HttpStatus.OK.value(), "OK",
				id );
	}

/*	private String obtenerUser(Integer numberUser, String nombreCompleto, String paterno) {
		String[] obtieneNombre = nombreCompleto.split(" ");
        String nombre = obtieneNombre[0];
        char[] apellido= paterno.toCharArray();
        char apM = apellido[0];
        String inicialApellido = String.valueOf(apM);
        String formatearCeros = String.format("%03d", numberUser);
		return nombre+inicialApellido+formatearCeros;
	}

	private String generarContrasenia(String nombreCompleto, String paterno) {
		Random random = new Random();
        String setOfCharacters = "#$^+=!*()@%&";
        int randomInt = random.nextInt(setOfCharacters.length());
        String[] obtieneNombre = nombreCompleto.split(" ");
        String nombre = obtieneNombre[0];
        for(int i=1; nombre.length()>i; i++  ) {
        	if(nombre.charAt(i)==nombre.charAt(i-1)) {
        	nombre=nombre.replace(nombre.charAt(i), setOfCharacters.charAt(randomInt));
        	}
        }
      
        char randomChar = setOfCharacters.charAt(randomInt);
        char[] apellido= paterno.toCharArray();
        
        char apM = apellido[0];
        String p3 = String.valueOf(apM); 
        char ap2= apellido[1];
        if(apM==ap2) {
        	ap2 = setOfCharacters.charAt(randomInt);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String date = sdf.format(new Date());
		
		return nombre+randomChar+"."+p3.toUpperCase()+ap2+date;
	} */
}
	

