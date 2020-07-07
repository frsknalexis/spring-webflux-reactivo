package com.dev.reactor.ws.dto;

import java.io.Serializable;

public class ValidationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8368527175775553174L;

	private String campo;
	
	private String mensaje;
	
	public ValidationDTO() {
		
	}
	
	public ValidationDTO(String campo, String mensaje) {
		this.campo = campo;
		this.mensaje = mensaje;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}