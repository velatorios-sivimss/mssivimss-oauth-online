package com.imss.sivimss.oauth.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.imss.sivimss.oauth.model.ContratanteDto;
import com.imss.sivimss.oauth.model.DomicilioDto;
import com.imss.sivimss.oauth.model.request.PersonaRequest;
import com.imss.sivimss.oauth.util.BdConstantes;
import com.imss.sivimss.oauth.util.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Slf4j
public class Contratante {
	
	private String idContratante;
	private String materno;
	private String nombre;
	private String correo;
	private String claveMatricula;
	private String password;
	private String paterno;
	private String statusCuenta;
	private String idVelatorio;
	private String idDelegacion;
	private String curp;
	private String claveUsuario;
	private String activo;
	
	public Contratante(Map<String, Object> datos) {
		this.idContratante = datos.get("ID_CONTRATANTE").toString();
		this.nombre = datos.get("NOM_PERSONA").toString();
		this.paterno = datos.get("NOM_PRIMER_APELLIDO").toString();
		this.materno = datos.get("NOM_SEGUNDO_APELLIDO").toString();
		if(datos.get("REF_CORREO") != null) {
			this.correo = datos.get("REF_CORREO").toString();
		}
		
		this.password = datos.get("CVE_CONTRASENIA").toString();
		
		if( datos.get("CVE_MATRICULA") != null ) {
			this.claveMatricula = datos.get("CVE_MATRICULA").toString();
		}
		
		if( datos.get("ID_DELEGACION") != null ) {
			this.idDelegacion = datos.get("ID_DELEGACION").toString();
		}
		
		if( datos.get("ID_VELATORIO") != null ) {
			this.idVelatorio = datos.get("ID_VELATORIO").toString();
		}
		
		this.curp = datos.get("CVE_CURP").toString();
		this.claveUsuario = datos.get("CVE_USUARIO").toString();
		this.activo = datos.get("IND_ACTIVO").toString();
	}
	
	
	public String buscarContratante(String user) {
		
		StringBuilder query = new StringBuilder(BdConstantes.SELECT_CONTRATANTE);
		query.append( "INNER JOIN SVC_PERSONA PER ON SC.ID_PERSONA = PER.ID_PERSONA " );
		query.append( BdConstantes.WHERE );
		query.append( BdConstantes.CVE_USUARIO + " = ");
		query.append( "'" + user + "' " );
		query.append( BdConstantes.LIMIT );
		
		return query.toString();
	}


	public String insertarPersona(PersonaRequest contratanteR) throws ParseException {
		final QueryHelper q ;
		String query="";
		if(contratanteR.getContratante().getIdContratante()==null) {
			q = new QueryHelper("INSERT INTO SVC_PERSONA");
		}else {
			q = new QueryHelper("UPDATE SVC_PERSONA");
		}
		q.agregarParametroValues("CVE_CURP", "'"+contratanteR.getCurp()+"'");
		q.agregarParametroValues("NOM_PERSONA", "'" +contratanteR.getNombre()+ "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" +contratanteR.getPaterno()+ "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" +contratanteR.getMaterno()+ "'");
		q.agregarParametroValues("CVE_RFC", setValor(contratanteR.getRfc()));
		q.agregarParametroValues("NUM_SEXO", contratanteR.getNumSexo().toString());	
		q.agregarParametroValues("REF_OTRO_SEXO", setValor(contratanteR.getOtroSexo()));
			Date dateF = new SimpleDateFormat("dd-MM-yyyy").parse(contratanteR.getFecNacimiento());
	        DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
		q.agregarParametroValues("FEC_NAC", "'"+fechaFormat.format(dateF)+"'");
		q.agregarParametroValues("ID_PAIS", contratanteR.getIdPais().toString());
		q.agregarParametroValues("ID_ESTADO", contratanteR.getIdLugarNac().toString());
		q.agregarParametroValues("REF_TELEFONO", setValor(contratanteR.getTel()));
		q.agregarParametroValues("REF_TELEFONO_FIJO", setValor(contratanteR.getTelFijo()));
		q.agregarParametroValues("REF_CORREO", setValor(contratanteR.getCorreo()));
		//q.agregarParametroValues(ID_USUARIO_MODIFICA, idUsuario.toString());
		if(contratanteR.getContratante().getIdContratante()==null) {
			q.agregarParametroValues("FEC_ALTA","CURRENT_DATE()");
			query = q.obtenerQueryInsertar();	
		}else {
			q.agregarParametroValues("FEC_ACTUALIZACION","CURRENT_DATE()");
			q.addWhere("ID_PERSONA="+contratanteR.getContratante().getIdPersona());
			query = q.obtenerQueryActualizar();	
		}
		
		log.info(query);
		return query;
	}


	public String insertarDomicilio(DomicilioDto domicilio, Integer idDomicilio) {
		final QueryHelper q ;
		String query="";
		if(idDomicilio==null) {
			q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		}else {
			q = new QueryHelper("UPDATE SVT_DOMICILIO");	
		}
			q.agregarParametroValues("REF_CALLE", setValor(domicilio.getCalle()));
			q.agregarParametroValues("NUM_EXTERIOR", setValor(domicilio.getNumExt()));
			q.agregarParametroValues("NUM_INTERIOR", setValor(domicilio.getNumInt()));
			q.agregarParametroValues("REF_CP", domicilio.getCp().toString());
			q.agregarParametroValues("REF_COLONIA", setValor(domicilio.getColonia()));
			q.agregarParametroValues("REF_MUNICIPIO", setValor(domicilio.getMunicipio()));
			q.agregarParametroValues("REF_ESTADO", setValor(domicilio.getEstado()));
			//q.agregarParametroValues(ID_USUARIO_MODIFICA, idUsuario.toString());
		if(idDomicilio==null) {
			q.agregarParametroValues("FEC_ALTA", "CURRENT_DATE()");
			query = q.obtenerQueryInsertar();
		}else {
			q.agregarParametroValues("FEC_ACTUALIZACION", "CURRENT_DATE()");
			q.addWhere("ID_DOMICILIO="+idDomicilio);
			query = q.obtenerQueryActualizar();
		}
		
		log.info("domicilio: "+query);
		return query;
	}


	public String insertarContratante(ContratanteDto contratante, String contrasenia, String user) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", contratante.getIdPersona().toString());
		q.agregarParametroValues("CVE_MATRICULA", setValor(contratante.getMatricula()));
		q.agregarParametroValues("ID_DOMICILIO", contratante.getIdDomicilio().toString());
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("CVE_CONTRASENIA", "'"+contrasenia+"'");
		q.agregarParametroValues("CVE_USUARIO", "'"+user+"'");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_DATE()");
		log.info("contratante: "+q.obtenerQueryInsertar());
		return q.obtenerQueryInsertar();
	}
	
	private String setValor(String valor) {
        if (valor==null || valor.equals("")) {
            return "NULL";
        }else {
            return "'"+valor+"'";
        }
    }


	public String cuentaUsuarios() {
		String query= "SELECT MAX(ID_CONTRATANTE)+1 AS max FROM SVC_CONTRATANTE";
		return query;
	}


}
