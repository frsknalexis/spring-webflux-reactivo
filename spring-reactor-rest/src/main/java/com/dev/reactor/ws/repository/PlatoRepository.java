package com.dev.reactor.ws.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.reactor.ws.base.repository.BaseRepository;
import com.dev.reactor.ws.document.Plato;

import reactor.core.publisher.Flux;

@Repository("platoRepository")
public interface PlatoRepository extends BaseRepository<Plato, String> {

	@Query("{ 'nombre': { $regex: ?0, '$options': 'i' } }")
	Flux<Plato> findByNombre(String term);
}