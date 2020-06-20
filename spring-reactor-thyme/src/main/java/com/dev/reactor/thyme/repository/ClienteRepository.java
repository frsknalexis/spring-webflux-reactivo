package com.dev.reactor.thyme.repository;

import org.springframework.stereotype.Repository;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.document.Cliente;

@Repository("clienteRepository")
public interface ClienteRepository extends BaseRepository<Cliente, String> {

}