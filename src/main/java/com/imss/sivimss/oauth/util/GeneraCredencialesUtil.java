package com.imss.sivimss.oauth.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GeneraCredencialesUtil {
	
	private static final Random RANDOM = new Random();

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
        String setOfCharacters = "#$^+=!*()@%&";
        int randomInt =RANDOM.nextInt(setOfCharacters.length());
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
	} 
	
}
