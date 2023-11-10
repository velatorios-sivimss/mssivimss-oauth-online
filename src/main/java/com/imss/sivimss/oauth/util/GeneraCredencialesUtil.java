package com.imss.sivimss.oauth.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneraCredencialesUtil {

	public String obtenerUser(Integer numberUser, String nombreCompleto, String paterno) {
		String[] obtieneNombre = nombreCompleto.split(" ");
        String nombre = obtieneNombre[0];
        char[] apellido= paterno.toCharArray();
        char apM = apellido[0];
        String inicialApellido = String.valueOf(apM);
        String formatearCeros = String.format("%03d", numberUser);
		return nombre+inicialApellido+formatearCeros;
	}

	public String generarContrasenia(String nombreCompleto, String paterno) {
		SecureRandom random = new SecureRandom();
        String caracteres = "#$^+=!*()@%&";
        int randomInt =random.nextInt(caracteres.length());
        String[] obtieneNombre = nombreCompleto.toLowerCase().split(" ");
        String nombre = obtieneNombre[0];
        for(int i=1; nombre.length()>i; i++  ) {
        	if(nombre.charAt(i)==nombre.charAt(i-1)) {
        	nombre=nombre.replace(nombre.charAt(i), caracteres.charAt(randomInt));
        	}
        }
      
        char randomChar = caracteres.charAt(randomInt);
        char[] apellido= paterno.toCharArray();
        
        char pLetra = apellido[0];//paterno 0
        String pLetraS = String.valueOf(pLetra); //TIENE EL MISMO VALOR QUE P3
        char sLetra= apellido[1];
        if(pLetra==sLetra) { //SEGUNDA LETRA PATERNO
        	sLetra = caracteres.charAt(randomInt);
        }
        SimpleDateFormat fecActual = new SimpleDateFormat("MM");
		String numMes = fecActual.format(new Date());
		
		return nombre.toUpperCase()+randomChar+"."+pLetraS.toUpperCase()+sLetra+numMes;
	} 
	
}
