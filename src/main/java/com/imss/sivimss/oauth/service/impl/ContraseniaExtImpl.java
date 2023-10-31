package com.imss.sivimss.oauth.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.oauth.beans.Contratante;
import com.imss.sivimss.oauth.beans.Usuario;
import com.imss.sivimss.oauth.exception.BadRequestException;
import com.imss.sivimss.oauth.model.Login;
import com.imss.sivimss.oauth.model.request.CorreoRequest;
import com.imss.sivimss.oauth.service.ContraseniaExtService;
import com.imss.sivimss.oauth.service.ContratanteService;
import com.imss.sivimss.oauth.service.CuentaExtService;
import com.imss.sivimss.oauth.service.CuentaService;
import com.imss.sivimss.oauth.service.UsuarioService;
import com.imss.sivimss.oauth.util.AppConstantes;
import com.imss.sivimss.oauth.util.BdConstantes;
import com.imss.sivimss.oauth.util.ConstantsMensajes;
import com.imss.sivimss.oauth.util.LogUtil;
import com.imss.sivimss.oauth.util.LoginUtil;
import com.imss.sivimss.oauth.util.MensajeEnum;
import com.imss.sivimss.oauth.util.ParametrosUtil;
import com.imss.sivimss.oauth.util.Response;

@Service
public class ContraseniaExtImpl extends UtileriaService implements ContraseniaExtService{

	@Value("${endpoints.envio-correo}")
	private String urlEnvioCorreo;
	
	@Autowired
	private CuentaExtService cuentaService;
	
	@Autowired
	private CuentaService cuentaServices;
	
	@Autowired
	private ContratanteService usuarioService;
	
	@Autowired
	private LogUtil logUtil;
	
	@Override
	public Response<Object> cambiar(String contratante, String contraAnterior, String contraNueva) throws Exception {
		//obtener datos del usuario
		Contratante usuario= usuarioService.obtener(contratante);
		Response<Object> resp;
		Boolean exito = false;
		
		if ( contraAnterior.equals(contraNueva) ) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Contraseñas iguales");
		}
		
		//obtener por clave
		Login login = cuentaService.obtenerLoginPorCveContratante( contratante );
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ login);
		
		if( !contraAnterior.isEmpty()) {
			validar(contratante, contraAnterior, contraNueva, login, usuario);
		}
		
		contraNueva = passwordEncoder.encode(contraNueva);
		exito = cuentaService.actualizarContra(login.getIdLogin(), login.getIdContratante(), contraNueva);
		
		if (Boolean.FALSE.equals(exito)) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Error al actualizar en BD");
		}
		
		resp =  new Response<>(false, HttpStatus.OK.value(), ConstantsMensajes.EXITO.getMensaje(),
				exito );
		
		return resp;
	}

	private void validar(String user, String contraAnterior, String contraNueva, Login login, Contratante usuario) throws Exception {
		
		
		Integer intentos = cuentaServices.validaNumIntentos(login.getIdLogin(), login.getFecBloqueo(), login.getNumIntentos());
		String mensaje = null;
		
		if ( !passwordEncoder.matches( contraAnterior, usuario.getPassword() ) && !contraAnterior.equals( usuario.getPassword() ) ) {
			intentos++;
			Integer maxNumIntentos = cuentaServices.actNumIntentos(login.getIdLogin(), intentos);
			
			if( intentos >= maxNumIntentos ) {
				mensaje =  MensajeEnum.INTENTOS_FALLIDOS.getValor();
			}else {
				mensaje = MensajeEnum.CONTRASENIA_INCORRECTA.getValor();
			}
			
			throw new BadRequestException(HttpStatus.BAD_REQUEST, mensaje);
			
		}else {
			cuentaServices.actNumIntentos(login.getIdLogin(), 0);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> generarCodigo(String contratante) throws IOException {
	Contratante usuario= usuarioService.obtener(contratante);
		
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"",CONSULTA+" "+ usuario);
		
		Login login = cuentaService.obtenerLoginPorIdContratante( usuario.getIdContratante() );
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
	

	@Override
	public Response<Object> validarCodigo(String user, String codigo) {

		return null;
	}
	
	
}
