package com.dev.reactor.ws.document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "facturas")
public class Factura implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8308804297103144881L;

	@Id
	private String id;
	
	@Field(name = "descripcion")
	private String descripcion;
	
	@Field(name = "observacion")
	private String observacion;
	
	@Field(name = "cliente")
	private Cliente cliente;
	
	@Field(name = "items")
	private List<FacturaItem> items;
	
	@Field(name = "creado_en")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime creadoEn;
	
	public Factura() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<FacturaItem> getItems() {
		return items;
	}

	public void setItems(List<FacturaItem> items) {
		this.items = items;
	}

	public LocalDateTime getCreadoEn() {
		return creadoEn;
	}

	public void setCreadoEn(LocalDateTime creadoEn) {
		this.creadoEn = creadoEn;
	}
}