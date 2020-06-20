package com.dev.reactor.thyme.service.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev.reactor.thyme.base.repository.BaseRepository;
import com.dev.reactor.thyme.base.service.BaseServiceImpl;
import com.dev.reactor.thyme.document.Cliente;
import com.dev.reactor.thyme.repository.ClienteRepository;
import com.dev.reactor.thyme.service.ClienteService;

import reactor.core.publisher.Flux;

@Service("clienteService")
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, String> implements ClienteService {

	private final ClienteRepository clienteRepository;
	
	public ClienteServiceImpl(@Autowired @Qualifier("clienteRepository") ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	@Override
	protected BaseRepository<Cliente, String> getRepository() {
		return clienteRepository;
	}

	@Override
	public Flux<Cliente> listarDemorado() {
		return clienteRepository.findAll()
				.repeat(30)
				.delayElements(Duration.ofSeconds(1));
	}

	@Override
	public Flux<Cliente> listarSobrecargado() {
		return clienteRepository.findAll()
				.repeat(500);
	}
}