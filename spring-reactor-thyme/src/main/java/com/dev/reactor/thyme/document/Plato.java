package com.dev.reactor.thyme.document;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "platos")
public class Plato implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9059575573513721463L;

	@Id
	private String id;
	
	@NotEmpty
	@Size(min = 3, max = 25, message = "Tamaño no permitido")
	@Field(name = "nombre")
	private String nombre;
	
	@NotNull
	@Max(value = 100)
	@Min(value = 1)
	@Field(name = "precio")
	private double precio;
	
	@NotNull
	@Field(name = "estado")
	private boolean estado;

	public Plato() {
		
	}

	public Plato(String id, String nombre, double precio, boolean estado) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.estado = estado;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}