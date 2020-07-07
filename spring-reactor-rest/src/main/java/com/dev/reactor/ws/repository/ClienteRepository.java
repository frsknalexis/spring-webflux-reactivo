package com.dev.reactor.ws.repository;

import org.springframework.stereotype.Repository;

import com.dev.reactor.ws.base.repository.BaseRepository;
import com.dev.reactor.ws.document.Cliente;

@Repository("clienteRepository")
public interface ClienteRepository extends BaseRepository<Cliente, String> {

}