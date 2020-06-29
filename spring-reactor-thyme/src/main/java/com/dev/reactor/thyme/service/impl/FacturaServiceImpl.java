package com.dev.reactor.thyme.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.base.service.BaseServiceImpl;
import com.dev.reactor.thyme.document.Factura;
import com.dev.reactor.thyme.repository.FacturaRepository;
import com.dev.reactor.thyme.service.FacturaService;

@Service("facturaService")
public class FacturaServiceImpl extends BaseServiceImpl<Factura, String> implements FacturaService {

	private final FacturaRepository facturaRepository;
	
	public FacturaServiceImpl(@Autowired @Qualifier("facturaRepository") FacturaRepository facturaRepository) {
		this.facturaRepository = facturaRepository;
	}
	
	@Override
	protected BaseRepository<Factura, String> getRepository() {
		return facturaRepository;
	}
}