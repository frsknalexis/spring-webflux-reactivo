package com.dev.reactor.ws.document;

import java.io.Serializable;

public class FacturaItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8630995464411104131L;

	private Integer cantidad;
	
	private Plato plato;
	
	public FacturaItem() {
		
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