package com.imss.sivimss.oauth.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.imss.sivimss.oauth.beans.Contratante;
import com.imss.sivimss.oauth.model.request.CorreoRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GeneraCredencialesUtil{

	@Autowired
	PasswordEncoder passwordEncoder;
	// PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	 @Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Value("${endpoints.envio-correo}")
	private String urlEnvioCorreo;

	public String insertarUser(Integer numberUser, String nombreCompleto, String paterno, String contrasenia, Integer idPersona, Statement statement) throws SQLException{
		String hash = passwordEncoder.encode(contrasenia);
		String[] obtieneNombre = nombreCompleto.split(" ");
        String nombre = obtieneNombre[0];
        char[] apellido= paterno.toCharArray();
        char apM = apellido[0];
        String inicialApellido = String.valueOf(apM);
        String formatearCeros = String.format("%03d", numberUser);
		String user = nombre+inicialApellido+formatearCeros;
		 statement.executeUpdate(insertarUsuario(idPersona, hash, user),
					Statement.RETURN_GENERATED_KEYS);
		return user;
	}

	public String generarContrasenia(String nombreCompleto, String paterno) {
		SecureRandom random = new SecureRandom();
        String caracteres = "#$^+=!*()@%&";
        int randomInt =random.nextInt(caracteres.length());
        String[] obtieneNombre = nombreCompleto.toLowerCase().split(" ");
        String nombre = obtieneNombre[0];
        for(int i=1; nombre.length()>i; i++  ) {
        	if(nombre.charAt(i)==nombre.charAt(i-1)) {
        		char letra = nombre.charAt(i);
        		char caracter = caracteres.charAt(randomInt);
        		nombre =nombre.replaceFirst(String.valueOf(letra), "?");
        	    nombre=nombre.replace(letra, caracter);
                nombre = nombre.replace("?", String.valueOf(letra));
            caracteres = caracteres.replace(String.valueOf(caracter), "");
        	}
        }
      
        char randomChar = caracteres.charAt(randomInt);
        char[] apellido= paterno.toCharArray();
        
        char pLetra = apellido[0];//paterno 0
        String pLetraS = String.valueOf(pLetra); 
        char sLetra= apellido[1];
        if(pLetra==sLetra) { //SEGUNDA LETRA PATERNO
        	sLetra = caracteres.charAt(randomInt);
        }
        SimpleDateFormat fecActual = new SimpleDateFormat("MM");
		String numMes = fecActual.format(new Date());
		
		return nombre.toUpperCase()+randomChar+"."+pLetraS.toUpperCase()+sLetra+numMes;
	} 
	
	public String insertarUsuario(Integer idPersona, String contrasenia, String user) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_USUARIOS");
		q.agregarParametroValues("ID_PERSONA", idPersona.toString());
		q.agregarParametroValues("ID_OFICINA", "3");
		q.agregarParametroValues("ID_ROL", "150");
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("CVE_CONTRASENIA", "'"+contrasenia+"'");
		q.agregarParametroValues("CVE_USUARIO", "'"+user+"'");
		q.agregarParametroValues("FEC_ALTA", BdConstantes.CURRENT_DATE);
		q.agregarParametroValues("IND_CONTRATANTE", "1");
		return q.obtenerQueryInsertar();
	}

	public Response<Object> enviarCorreo(String user, String correo, String nombre, String paterno, String materno,
			String contrasenia) throws IOException {
		log.info("envioCorreo "+urlEnvioCorreo);
		String credenciales = "<b>Nombre completo del Usuario:</b> "+nombre+" "+paterno+" "+materno+"<br> <b>Clave de usuario: </b>"+user +"<br> <b>Contraseña: </b>"+contrasenia;
		CorreoRequest correoR = new CorreoRequest(user, credenciales, correo, AppConstantes.USR_CONTRASENIA);
			//Hacemos el consumo para enviar el codigo por correo
		
			Response <Object>response = providerRestTemplate.consumirServicio(correoR, urlEnvioCorreo);
		
		if(response.getCodigo()!=200) {
			return new Response<>(true, 200, "Error en el envio de correos", null);
		}
		  return response;
	}
	
	
	
}
