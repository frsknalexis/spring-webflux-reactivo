package com.dev.reactor.thyme.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev.reactor.thyme.document.Plato;
import com.dev.reactor.thyme.repository.PlatoRepository;
import com.dev.reactor.thyme.service.PlatoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("platoService")
public class PlatoServiceImpl implements PlatoService {

	private final PlatoRepository platoRepository;
	
	public PlatoServiceImpl(@Autowired @Qualifier("platoRepository") PlatoRepository platoRepository) {
		this.platoRepository = platoRepository;
	}
	
	@Override
	public Mono<Plato> registrar(Plato plato) {
		return platoRepository.save(plato);
	}

	@Override
	public Mono<Plato> modificar(Plato plato) {
		return platoRepository.save(plato);
	}

	@Override
	public Flux<Plato> listar() {
		return platoRepository.findAll();
	}

	@Override
	public Mono<Plato> listarPorId(String id) {
		return platoRepository.findById(id);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return platoRepository.findById(id)
							.flatMap((p) -> {
								return platoRepository.delete(p);
							});
	}

}
