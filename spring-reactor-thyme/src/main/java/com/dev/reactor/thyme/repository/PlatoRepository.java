package com.dev.reactor.thyme.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.dev.reactor.thyme.document.Plato;

@Repository("platoRepository")
public interface PlatoRepository extends ReactiveMongoRepository<Plato, String> {

}