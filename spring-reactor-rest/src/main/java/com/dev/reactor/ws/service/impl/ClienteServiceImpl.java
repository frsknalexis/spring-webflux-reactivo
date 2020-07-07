package com.dev.reactor.ws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev.reactor.ws.base.repository.BaseRepository;
import com.dev.reactor.ws.base.service.BaseServiceImpl;
import com.dev.reactor.ws.document.Cliente;
import com.dev.reactor.ws.repository.ClienteRepository;
import com.dev.reactor.ws.service.ClienteService;

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
}