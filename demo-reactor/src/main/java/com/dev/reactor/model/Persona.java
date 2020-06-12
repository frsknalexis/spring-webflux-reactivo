package com.dev.reactor.model;

public class Persona {

	private Integer idPersona;
	
	private String nombres;
	
	private Integer edad;
	
	public Persona() {
		
	}

	public Persona(Integer idPersona, String nombres, Integer edad) {
		this.idPersona = idPersona;
		this.nombres = nombres;
		this.edad = edad;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	@Override
	public String toString() {
		return "Persona [idPersona=" + idPersona + ", nombres=" + nombres + ", edad=" + edad + "]";
	}
}