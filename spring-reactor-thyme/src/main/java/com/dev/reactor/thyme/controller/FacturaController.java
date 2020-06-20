package com.dev.reactor.thyme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.reactor.thyme.document.Cliente;
import com.dev.reactor.thyme.pagination.PageSupport;
import com.dev.reactor.thyme.service.ClienteService;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

	private final ClienteService clienteService;
	
	public FacturaController(@Autowired @Qualifier("clienteService") ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@GetMapping("/listar")
	public Mono<String> listarPageable(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
		int pageSize = 1;
		Pageable pageable = PageRequest.of(page, pageSize);
		Mono<PageSupport<Cliente>> monoPage = clienteService.listarPageable(pageable);
		model.addAttribute("pageSp", monoPage);
		model.addAttribute("url", "/facturas/listar");
		return Mono.just("facturas/listar");
	}
}