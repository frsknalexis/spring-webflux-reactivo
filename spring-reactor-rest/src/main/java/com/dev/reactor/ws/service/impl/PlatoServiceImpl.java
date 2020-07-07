package com.dev.reactor.ws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev.reactor.ws.base.repository.BaseRepository;
import com.dev.reactor.ws.base.service.BaseServiceImpl;
import com.dev.reactor.ws.document.Plato;
import com.dev.reactor.ws.repository.PlatoRepository;
import com.dev.reactor.ws.service.PlatoService;

import reactor.core.publisher.Flux;

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
	
	@Override
	public Flux<Plato> buscarPorNombre(String term) {
		return platoRepository.findByNombre(term);
	}
}