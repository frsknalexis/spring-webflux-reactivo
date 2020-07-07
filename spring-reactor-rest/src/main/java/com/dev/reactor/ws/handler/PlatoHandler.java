package com.dev.reactor.ws.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dev.reactor.ws.document.Plato;
import com.dev.reactor.ws.dto.ValidationDTO;
import com.dev.reactor.ws.service.PlatoService;
import com.dev.reactor.ws.validator.RequestValidator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PlatoHandler {

	private final PlatoService platoService;
	
	private final Validator validator;
	
	private final RequestValidator requestValidator;
	
	public PlatoHandler(@Autowired @Qualifier("platoService") PlatoService platoService, @Autowired Validator validator,
			@Autowired RequestValidator requestValidator) {
		this.platoService = platoService;
		this.validator = validator;
		this.requestValidator = requestValidator;
	}
	
	public Mono<ServerResponse> listar(ServerRequest request) {
		Flux<Plato> fluxPlatos = platoService.listar();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fluxPlatos, Plato.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest request) {
		String platoId = request.pathVariable("platoId");
		return platoService.listarPorId(platoId)
				.flatMap(plato -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(plato)))
				.switchIfEmpty(ServerResponse.notFound()
						.build());
	}
	
	public Mono<ServerResponse> registrar(ServerRequest request) {
		Mono<Plato> platoRequest = request.bodyToMono(Plato.class);
		
		return platoRequest
				.flatMap(p -> {
					Errors errors = new BeanPropertyBindingResult(p, Plato.class.getName());
					validator.validate(p, errors);
					
					if (errors.hasErrors()) {
						return Flux.fromIterable(errors.getFieldErrors())
								.map(error -> new ValidationDTO(error.getField(), error.getDefaultMessage()))
								.collectList()
								.flatMap(lista -> ServerResponse.badRequest()
										.contentType(MediaType.APPLICATION_JSON)
										.body(fromValue(lista)));
					} else {
						return platoService.registrar(p)
								.flatMap(plato -> ServerResponse.created(URI.create(request.uri().toString().concat("/")
										.concat(plato.getId())))
										.contentType(MediaType.APPLICATION_JSON)
										.body(fromValue(plato)));
					}
				});
	}
	
	public Mono<ServerResponse> modificar(ServerRequest request) {
		Mono<Plato> platoRequest = request.bodyToMono(Plato.class);
		return platoRequest
				// VALIDACION ATRAVES DE UNA CLASE REQUEST VALIDATOR
				.flatMap(p -> requestValidator.validate(p))
				.flatMap(p -> platoService.modificar(p))
				.flatMap(p -> ServerResponse.created(URI.create(request.uri().toString().concat("/")
						.concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p)));
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request) {
		String platoId = request.pathVariable("platoId");
		return platoService.listarPorId(platoId)
				.flatMap(p -> platoService.eliminar(p.getId())
						.then(ServerResponse.noContent()
								.build()))
				.switchIfEmpty(ServerResponse.notFound()
						.build());
	}
}