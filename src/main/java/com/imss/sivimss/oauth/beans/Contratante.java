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
	private String idUsuario;
	private String idPersona;
	private String materno;
	private String nombre;
	private String correo;
	private String claveMatricula;
	private String password;
	private String paterno;
	private String statusCuenta;
	private String idPais;
	private String idEstado;
	private String curp;
	private String claveUsuario;
	private String activo;
	private String idRol;
	private String rol;
	
	
	public Contratante(Map<String, Object> datos) {
		this.idContratante = datos.get("ID_CONTRATANTE").toString();
        if(datos.get("ID_USUARIO")!=null) {
        	this.idUsuario = datos.get("ID_USUARIO").toString();
        }
		this.idPersona = datos.get(BdConstantes.ID_PERSONA).toString();
		this.nombre = datos.get("NOM_PERSONA").toString();
		this.paterno = datos.get("NOM_PRIMER_APELLIDO").toString();
		this.materno = datos.get("NOM_SEGUNDO_APELLIDO").toString();
		if(datos.get(BdConstantes.REF_CORREO) != null) {
			this.correo = datos.get(BdConstantes.REF_CORREO).toString();
		}
		if(datos.get(BdConstantes.CVE_CONTRASENIA)!=null) {
			this.password = datos.get(BdConstantes.CVE_CONTRASENIA).toString();
		}
		if( datos.get(BdConstantes.CVE_MATRICULA) != null ) {
			this.claveMatricula = datos.get(BdConstantes.CVE_MATRICULA).toString();
		}
			this.idPais = datos.get("ID_PAIS").toString();
		if( datos.get(BdConstantes.ID_ESTADO) != null ) {
			this.idEstado = datos.get(BdConstantes.ID_ESTADO).toString();
		}
		this.curp = datos.get("CVE_CURP").toString();
		if(datos.get(BdConstantes.CVE_USUARIO)!=null) {
			this.claveUsuario = datos.get(BdConstantes.CVE_USUARIO).toString();
		}
		this.activo = datos.get(BdConstantes.IND_ACTIVO).toString();
		this.idRol = datos.get("ID_ROL").toString();
		this.rol = datos.get("DES_ROL").toString();
	}
	
	
	public String buscarContratante(String user) {
		
		StringBuilder query = new StringBuilder(BdConstantes.SELECT_CONTRATANTE);
		query.append( "INNER JOIN SVC_PERSONA PER ON SC.ID_PERSONA = PER.ID_PERSONA " );
		query.append( "INNER JOIN SVT_USUARIOS USR ON PER.ID_PERSONA = USR.ID_PERSONA " );
		query.append( "INNER JOIN SVC_ROL ROL ON USR.ID_ROL = ROL.ID_ROL " );
		query.append( BdConstantes.WHERE );
		query.append( BdConstantes.CVE_USUARIO + " = ");
		query.append( "'" + user + "' " );
		query.append( BdConstantes.LIMIT );
		
		return query.toString();
	}


	public String insertarPersona(PersonaRequest contratanteR) throws ParseException {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_PERSONA");
		q.agregarParametroValues("CVE_CURP", "'"+contratanteR.getCurp()+"'");
		q.agregarParametroValues("NOM_PERSONA", "'" +contratanteR.getNombre()+ "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" +contratanteR.getPaterno()+ "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" +contratanteR.getMaterno()+ "'");
		q.agregarParametroValues("CVE_RFC", setValor(contratanteR.getRfc()));
		q.agregarParametroValues("NUM_SEXO", contratanteR.getNumSexo().toString());	
		q.agregarParametroValues("REF_OTRO_SEXO", setValor(contratanteR.getOtroSexo()));
		q.agregarParametroValues("TIP_PERSONA", "CONTRATANTE");
			Date dateF = new SimpleDateFormat("dd-MM-yyyy").parse(contratanteR.getFecNacimiento());
	        DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
		q.agregarParametroValues("FEC_NAC", "'"+fechaFormat.format(dateF)+"'");
		q.agregarParametroValues("ID_PAIS", ""+contratanteR.getIdPais()+"");
		q.agregarParametroValues(BdConstantes.ID_ESTADO, ""+contratanteR.getIdLugarNac()+"");
		q.agregarParametroValues("REF_TELEFONO", setValor(contratanteR.getTel()));
		q.agregarParametroValues("REF_TELEFONO_FIJO", setValor(contratanteR.getTelFijo()));
		q.agregarParametroValues(BdConstantes.REF_CORREO, setValor(contratanteR.getCorreo()));
		//q.agregarParametroValues(ID_USUARIO_ALTA, idUsuario.toString());
			q.agregarParametroValues(BdConstantes.FEC_ALTA,BdConstantes.CURRENT_DATE);
			return q.obtenerQueryInsertar();	
	}


	public String insertarDomicilio(DomicilioDto domicilio, Integer idUsuario) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
			q.agregarParametroValues("REF_CALLE", setValor(domicilio.getCalle()));
			q.agregarParametroValues("NUM_EXTERIOR", setValor(domicilio.getNumExt()));
			q.agregarParametroValues("NUM_INTERIOR", setValor(domicilio.getNumInt()));
			q.agregarParametroValues("REF_CP", domicilio.getCp().toString());
			q.agregarParametroValues("REF_COLONIA", setValor(domicilio.getColonia()));
			q.agregarParametroValues("REF_MUNICIPIO", setValor(domicilio.getMunicipio()));
			q.agregarParametroValues("REF_ESTADO", setValor(domicilio.getEstado()));
			//q.agregarParametroValues(BdConstantes.ID_USUARIO_ALTA, idUsuario.toString());
			q.agregarParametroValues(BdConstantes.FEC_ALTA, BdConstantes.CURRENT_DATE);
			return q.obtenerQueryInsertar();
	}


	public String insertarContratante(ContratanteDto contratante) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues(BdConstantes.ID_PERSONA, contratante.getIdPersona().toString());
		q.agregarParametroValues(BdConstantes.CVE_MATRICULA, setValor(contratante.getMatricula()));
		q.agregarParametroValues("ID_DOMICILIO", contratante.getIdDomicilio().toString());
		q.agregarParametroValues("IND_ACTIVO", "1");
		//q.agregarParametroValues(BdConstantes.ID_USUARIO_ALTA, contratante.getIdUsuario().toString());
		q.agregarParametroValues(BdConstantes.FEC_ALTA, BdConstantes.CURRENT_DATE);
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
		return BdConstantes.MAX_CONTRATANTE;
	}


	public String consultaPlanSFPA(Integer idPlanSfpa) {
		log.info(" INICIO - consultaPlanSFPA");
		StringBuilder queryUtil = new StringBuilder();
		queryUtil.append(" SELECT  SPSFPA.ID_TITULAR as idTitular, SP.ID_PERSONA AS idPersona, IFNULL(SP.NOM_PERSONA, '') AS nomPersona, ")
		.append(" IFNULL(SP.NOM_PRIMER_APELLIDO, '') AS nomApellidoPaterno, IFNULL(SP.NOM_SEGUNDO_APELLIDO, '')AS nomApellidoMaterno, ")
		.append(" IFNULL(SP.REF_CORREO , '')AS correo FROM SVT_PLAN_SFPA SPSFPA INNER JOIN SVC_CONTRATANTE SC ON ")
		.append(" SC.ID_CONTRATANTE = SPSFPA.ID_TITULAR INNER JOIN SVC_PERSONA SP ON SP.ID_PERSONA = SC.ID_PERSONA ")
		.append(" WHERE SPSFPA.ID_PLAN_SFPA = ").append(idPlanSfpa);
		log.info(" TERMINO - consultaPlanSFPA"+ queryUtil.toString() );
		return queryUtil.toString();
	}

}
