package com.dev.reactor.thyme.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class LoginController {

	@GetMapping(value = { "/", "/login" })
	public Mono<String> login(Principal principal) {
		if (principal != null) {
			return Mono.just("redirect:/clientes/listar");
		}
		return Mono.just("login");
	}
}