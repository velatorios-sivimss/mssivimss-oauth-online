package com.imss.sivimss.oauth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LogUtil {
    @Value("${ruta-log}")
    private String rutaLog;

    @Value("${spring.application.name}")
    private String nombre;
    
    private String formatoFechaLog = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogUtil.class);
    
    private static String RETORNO = "\r\n";
    
    public void crearArchivoLog(String tipoLog, String origen, String clasePath, String mensaje, String tiempoEjecucion) throws IOException {
    	
        try(FileWriter escribirArchivo = new FileWriter(new File(rutaLog + nombre + new SimpleDateFormat("ddMMyyyy").format(new Date()) + ".log"), true) ) {
        	
        	String peticion = "" + formatoFechaLog + " --- [" + tipoLog + "] " + origen + " " + clasePath + " : " + mensaje + " - " + tiempoEjecucion;
        	log.info(peticion);
            escribirArchivo.write(peticion);
            escribirArchivo.write(RETORNO);
            escribirArchivo.close();
            
        } catch (Exception e) {
            log.error("No se puede escribir el log.");
            log.error(e.getMessage());
        }

    }

    public void crearArchivoLogDTO(String tipoLog, String origen, String clasePath, String mensaje, String tiempoEjecucion) throws IOException {
       
        try( FileWriter escribirArchivo = new FileWriter(new File(rutaLog + nombre + new SimpleDateFormat("ddMMyyyy").format(new Date()) + ".log"), true) ) {
            
        	String peticion = "" + formatoFechaLog + " --- [" + tipoLog + "] " + origen + " " + clasePath + " : " + mensaje + " - " + tiempoEjecucion;
        	log.info(peticion);
            escribirArchivo.write(peticion);
            escribirArchivo.write(RETORNO);
            escribirArchivo.close();
            
        } catch (Exception e) {
            log.error("No se puede escribir el log.");
            log.error(e.getMessage());
        }

    }
}
