package com.dev.reactor.thyme.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.base.service.BaseServiceImpl;
import com.dev.reactor.thyme.document.Plato;
import com.dev.reactor.thyme.repository.PlatoRepository;
import com.dev.reactor.thyme.service.PlatoService;

@Service("platoService")
public class PlatoServiceImpl extends BaseServiceImpl<Plato, String> implements PlatoService {

	private final PlatoRepository platoRepository;
	
	public PlatoServiceImpl(@Autowired @Qualifier("platoRepository") PlatoRepository platoRepository) {
		this.platoRepository = platoRepository;
	}
	
	@Override
	protected BaseRepository<Plato, String> getRepository() {
		return platoRepository;
	}
}