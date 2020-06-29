package com.dev.reactor.thyme.document;

import java.io.Serializable;

public class FacturaItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1654792891322184199L;

	private Integer cantidad;
	
	private Plato plato;
	
	public FacturaItem() {
		
	}
	
	public Double calcularMonto() {
		return cantidad.doubleValue() * plato.getPrecio();
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Plato getPlato() {
		return plato;
	}

	public void setPlato(Plato plato) {
		this.plato = plato;
	}
}