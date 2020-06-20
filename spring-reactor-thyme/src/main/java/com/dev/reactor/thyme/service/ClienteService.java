package com.dev.reactor.thyme.service;

import com.dev.reactor.thyme.base.service.BaseService;
import com.dev.reactor.thyme.document.Cliente;

import reactor.core.publisher.Flux;

public interface ClienteService extends BaseService<Cliente, String> {

	Flux<Cliente> listarDemorado();
	
	Flux<Cliente> listarSobrecargado();
}