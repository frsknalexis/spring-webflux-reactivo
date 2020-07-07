package com.dev.reactor.ws.service;

import com.dev.reactor.ws.base.service.BaseService;
import com.dev.reactor.ws.document.Plato;

import reactor.core.publisher.Flux;

public interface PlatoService extends BaseService<Plato, String> {

	Flux<Plato> buscarPorNombre(String term);
}