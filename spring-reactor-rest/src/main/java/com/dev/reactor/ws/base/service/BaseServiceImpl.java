package com.dev.reactor.ws.base.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;

import com.dev.reactor.ws.base.repository.BaseRepository;
import com.dev.reactor.ws.pagination.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

	protected abstract BaseRepository<T, ID> getRepository();
	
	@Override
	public Mono<T> registrar(T t) {
		return getRepository().save(t);
	}

	@Override
	public Mono<T> modificar(T t) {
		return getRepository().save(t);
	}

	@Override
	public Flux<T> listar() {
		return getRepository().findAll();
	}

	@Override
	public Mono<PageSupport<T>> listarPageable(Pageable pageable) {
		return getRepository().findAll()
				.collectList()
				.map(lista -> {
					return new PageSupport<>(lista.stream()
							.skip(pageable.getPageNumber() * pageable.getPageSize())
							.limit(pageable.getPageSize())
							.collect(Collectors.toList()), pageable.getPageNumber(), 
							pageable.getPageSize(), lista.size());
				});
	}

	@Override
	public Mono<T> listarPorId(ID id) {
		return getRepository().findById(id);
	}

	@Override
	public Mono<Void> eliminar(ID id) {
		return getRepository().findById(id)
				.flatMap(t -> {
					return getRepository().delete(t);
				});
	}	
}