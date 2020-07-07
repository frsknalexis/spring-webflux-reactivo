package com.dev.reactor.ws.document;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Datos para Platos")
@Document(collection = "platos")
public class Plato implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2372905251087777443L;

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
	private Double precio;
	
	@NotNull
	@Field(name = "estado")
	private Boolean estado;

	public Plato() {
		
	}
	
	public Plato(String id) {
		this.id = id;
	}

	public Plato(String id, String nombre, Double precio, boolean estado) {
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

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}