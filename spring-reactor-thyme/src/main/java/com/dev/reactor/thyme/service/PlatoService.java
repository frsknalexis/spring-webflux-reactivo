package com.dev.reactor.thyme.service;

import com.dev.reactor.thyme.base.service.BaseService;
import com.dev.reactor.thyme.document.Plato;

import reactor.core.publisher.Flux;

public interface PlatoService extends BaseService<Plato, String> {

	Flux<Plato> buscarPorNombre(String term);
}