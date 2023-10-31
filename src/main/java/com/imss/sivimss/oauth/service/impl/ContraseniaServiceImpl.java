package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.beans.Usuario;
import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.model.Login;
import com.imss.sivimss.oauth.model.request.CorreoRequest;
import com.imss.sivimss.oauth.service.ContraseniaService;
import com.imss.sivimss.oauth.service.CuentaService;
import com.imss.sivimss.oauth.service.UsuarioService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.BdConstantes;
import com.imss.sivimss.oauth.util.ConstantsMensajes;
import com.imss.sivimss.oauth.util.EstatusVigenciaEnum;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.LoginUtil;
import com.imss.sivimss.oauth.util.MensajeEnum;
import com.imss.sivimss.oauth.util.ParametrosUtil;
import com.imss.sivimss.oauth.util.Response;

@Service
public class ContraseniaServiceImpl extends UtileriaService implements ContraseniaService {
	
	@Value("${endpoints.envio-correo}")
	private String urlEnvioCorreo;
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private LogUtil logUtil;
	
	@Override
	public Response<Object> cambiar(String user, String contraAnterior, String contraNueva) throws Exception {
		//obtener datos del usuario
		Usuario usuario= usuarioService.obtener(user);
		Response<Object> resp;
		Boolean exito = false;
		
		if ( contraAnterior.equals(contraNueva) ) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Contraseñas iguales");
		}
		
		//obtener por clave
		Login login = cuentaService.obtenerLoginPorCveUsuario( user );
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ login);
		
		if( !contraAnterior.isEmpty()) {
			validar(user, contraAnterior, contraNueva, login, usuario);
		}
		
		contraNueva = passwordEncoder.encode(contraNueva);
		exito = cuentaService.actualizarContra(login.getIdLogin(), login.getIdUsuario(), contraNueva);
		
		if (Boolean.FALSE.equals(exito)) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Error al actualizar en BD");
		}
		
		resp =  new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				exito );
		
		return resp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer validarFecha(String fecha) throws Exception {
		List<Map<String, Object>> datos;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		List<Map<String, Object>> mapping;
		String sNumDias;
		Integer numDias;
		String sNumMeses;
		Integer numMeses;
		Date fechaActual;
		Date fechaProxVencer;
		Date fechaVencida;
		SimpleDateFormat formatter;
		Calendar calendar = Calendar.getInstance();
		Integer estatus = EstatusVigenciaEnum.VALIDA.getId();
		
		datos = consultaGenericaPorQuery( parametrosUtil.numDias() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		sNumDias = mapping.get(0).get(BdConstantes.TIP_PARAMETRO).toString();
		numDias = Integer.parseInt(sNumDias);
		//numDias = -15 dias
		numDias = numDias * (-1);
		
		datos = consultaGenericaPorQuery( parametrosUtil.numMeses() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		sNumMeses = mapping.get(0).get(BdConstantes.TIP_PARAMETRO).toString();
		//numMeses = 3 meses
		numMeses = Integer.parseInt(sNumMeses);
		//yyyy-MM-dd HH:mm:ss
		formatter = new SimpleDateFormat(PATTERN);
		//fecha = fecha cambio contrasenia
		fechaActual = formatter.parse(fecha);
		
		calendar.setTime(fechaActual);
		calendar.add(Calendar.MONTH , numMeses);
		//se le agregan 3 meses ala fecha del cambio de contrasenia para obtener la fecha vencida
		fechaVencida = calendar.getTime();
		
		calendar.add(Calendar.DAY_OF_YEAR, numDias);
		//se le restan 15 dias a fecha que vence para saber cuando estara proxima a vencer
		fechaProxVencer = calendar.getTime();
		//TIEMPO
		datos = consultaGenericaPorQuery( parametrosUtil.obtenerFecha(formatoSQL) );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		String tiempoSQL = mapping.get(0).get("tiempo").toString();
		formatter = new SimpleDateFormat(patronSQL);
        //FECCHA ACTUAL = CURRENT_TIMESTAMP
		fechaActual =  formatter.parse(tiempoSQL);
		
		//si la fecha actual esta despues de proxima a vencer y fecha actual esta antes que la vencida
		if( fechaActual.after(fechaProxVencer) && fechaActual.before(fechaVencida) ) {
			estatus = EstatusVigenciaEnum.PROXIMA_VENCER.getId();
			// fecha actual esta despues que la vencida
		}else if( fechaActual.after(fechaVencida) ) {
			estatus = EstatusVigenciaEnum.VENCIDA.getId();
		}
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ estatus);
		
		return estatus;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> generarCodigo(String user) throws IOException {
		//obtiene datos user
		Usuario usuario= usuarioService.obtener(user);
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ usuario);
		
		Login login = cuentaService.obtenerLoginPorIdUsuario( usuario.getIdUsuario() );
		List<Map<String, Object>> datos;
		List<Map<String, Object>> mapping;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		Integer longitud;
		LoginUtil loginUtil = new LoginUtil();
		String codigo;
		Response<Object> resp;
		
		datos = consultaGenericaPorQuery( parametrosUtil.longCodigo() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		
		//SE OBTIENE DE BD LA LONGITUD = 8
		longitud = Integer.parseInt(mapping.get(0).get(BdConstantes.TIP_PARAMETRO).toString());
		
		codigo = loginUtil.generarCodigo(longitud);
		
		CorreoRequest correo = new CorreoRequest(usuario.getNombre(), codigo, usuario.getCorreo(), AppConstantes.TIPO_CORREO);
		
		//Hacemos el consumo para enviar el codigo por correo
		resp = providerRestTemplate.consumirServicio(correo, urlEnvioCorreo);
		
		if (resp.getCodigo() != 200) {
			return resp;
		}
		
		//Guardamos el codigo en la BD y Guardamos el historial
		List<String> querys = new ArrayList<>();
		//update en SVT_LOGIN
		querys.add( loginUtil.actCodSeg(login.getIdLogin(), codigo) );
		//INSERT EN SVT_HIST_CODIGO_SEGURIDAD
		querys.add( loginUtil.historial(login.getIdLogin(), codigo) );
		
		Boolean exito = actualizarMultiple( querys );
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(),"",CONSULTA+" "+ exito.toString());
		
		
		
		Map<String, String> salida = new HashMap<>();
		salida.put("correo", usuario.getCorreo());
		
		resp =  new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				salida );
		
		return resp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> validarCodigo(String user, String codigo) throws Exception {
		//Obtenemos el codigo desde BD
		Login login = cuentaService.obtenerLoginPorCveUsuario( user );
		List<Map<String, Object>> datos;
		List<Map<String, Object>> mapping;
		ParametrosUtil parametrosUtil = new ParametrosUtil();
		Response<Object> resp = null;
		LoginUtil loginUtil = new LoginUtil();
		
		datos = consultaGenericaPorQuery( parametrosUtil.tiempoCodigo() );
		mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
		//tiempoCodigo = 390 *6.5 HRS DEBE DURAR 30 MINS
		Integer tiempoCodigo = Integer.parseInt(mapping.get(0).get(BdConstantes.TIP_PARAMETRO).toString());
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(),"","Tiempo Vida Codigo "+ tiempoCodigo);
		
		
		//SI EL CODIGO TRAE DATOS PERO NO SON CORRECTO
		if( login.getCodSeguridad()!=null && !login.getCodSeguridad().equals(codigo) ) {
			
			//Validamos si es un codigo anterior
			datos = consultaGenericaPorQuery( loginUtil.conteo( login.getIdLogin(), codigo ) );
			mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
			
			Integer conteo = Integer.parseInt(mapping.get(0).get("conteo").toString());
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Conteo de Codigos "+ conteo);
			
			if(conteo >=1 ) {
				
				resp =  new Response<>(false, HttpStatus.OK.value(), MensajeEnum.CODIGO_EXPIRADO.getValor(),
						null );
				
			}else {
				
				resp =  new Response<>(false, HttpStatus.OK.value(), MensajeEnum.CODIGO_INCORRECTO.getValor(),
						null );
				
			}
			
			return resp;
		}
		
		
		//CUANDO EL CODIGO ES CORRECTO
		if( login.getFecCodSeguridad() != null && !login.getFecCodSeguridad().isEmpty() ) {
			
			//Validando vigencia del Codigo de Autenticacion
			datos = consultaGenericaPorQuery( loginUtil.difTiempo( login.getIdLogin() ) );
			mapping = Arrays.asList(modelMapper.map(datos, HashMap[].class));
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Datos Codigo Seguridad "+ mapping);
			
			Integer diferencia = Integer.parseInt(mapping.get(0).get("diferencia").toString());
			
			logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"","Diferencia de Tiempo "+ diferencia);
			
			// 198498 <= 390
			if( diferencia <= tiempoCodigo ) {
				resp =  new Response<>(false, HttpStatus.OK.value(), MensajeEnum.CODIGO_CORRECTO.getValor(),
						null );
			}else {
				resp =  new Response<>(false, HttpStatus.OK.value(), MensajeEnum.CODIGO_EXPIRADO.getValor(),
						null );
			}
		}
		
		return resp;
	}
	
	private void validar(String user, String contraAnterior, String contraNueva, Login login, Usuario usuario) throws Exception {
		
		
		Integer intentos = cuentaService.validaNumIntentos(login.getIdLogin(), login.getFecBloqueo(), login.getNumIntentos());
		String mensaje = null;
		
		if ( !passwordEncoder.matches( contraAnterior, usuario.getPassword() ) && !contraAnterior.equals( usuario.getPassword() ) ) {
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
		
	}
	
}
