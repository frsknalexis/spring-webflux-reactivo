package com.dev.reactor.ws.base.service;

import org.springframework.data.domain.Pageable;

import com.dev.reactor.ws.pagination.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BaseService<T, ID> {

	Mono<T> registrar(T t);
	
	Mono<T> modificar(T t);
	
	Flux<T> listar();
	
	Mono<PageSupport<T>> listarPageable(Pageable pageable);
	
	Mono<T> listarPorId(ID id);
	
	Mono<Void> eliminar(ID id);
}