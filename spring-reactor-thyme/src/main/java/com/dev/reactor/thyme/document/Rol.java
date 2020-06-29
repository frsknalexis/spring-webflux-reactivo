package com.dev.reactor.thyme.document;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
public class Rol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2754180248843439458L;

	@Id
	private String id;
	
	@Field(name = "nombre")
	private String nombre;
	
	public Rol() {
		
	}
	
	public Rol(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}