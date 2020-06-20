package com.dev.reactor.thyme.repository;

import org.springframework.stereotype.Repository;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.document.Plato;

@Repository("platoRepository")
public interface PlatoRepository extends BaseRepository<Plato, String> {

}