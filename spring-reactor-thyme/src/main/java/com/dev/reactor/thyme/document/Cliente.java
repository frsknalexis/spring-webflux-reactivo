package com.dev.reactor.thyme.document;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "clientes")
public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -36918037517535784L;

	@Id
	private String id;
	
	@NotEmpty
	@Field(name = "nombres")
	private String nombres;
	
	@NotEmpty
	@Field(name = "apellidos")
	private String apellidos;
	
	@NotNull(message = "Campo Obligatorio")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Field(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;
	
	@Field(name = "url_foto")
	private String urlFoto;

	public Cliente() {
		
	}

	public Cliente(String id, String nombres, String apellidos, LocalDate fechaNacimiento, String urlFoto) {
		this.id = id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.urlFoto = urlFoto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
}