package com.dev.reactor.ws.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.reactor.ws.document.Plato;
import com.dev.reactor.ws.pagination.PageSupport;
import com.dev.reactor.ws.service.PlatoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static reactor.function.TupleUtils.function;

@RestController
@RequestMapping("/platos")
public class PlatoRestController {

	private final PlatoService platoService;
	
	public PlatoRestController(@Autowired @Qualifier("platoService") PlatoService platoService) {
		this.platoService = platoService;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Flux<Plato>>> listar() {
		Flux<Plato> platosFlux = platoService.listar();
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(platosFlux));
	}
	
	@GetMapping(value = "/{platoId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Plato>> listarPorId(@PathVariable(value = "platoId") String platoId) {
		return platoService.listarPorId(platoId)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound()
						.build());
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Plato>> registrar(@Valid @RequestBody Plato plato, ServerHttpRequest request) {
		return platoService.registrar(plato)
				.map(p -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/")
						.concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p));
	}
	
	@PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Plato>> modificar(@Valid @RequestBody Plato plato, ServerHttpRequest request) {
		return platoService.modificar(plato)
				.map(p -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/")
						.concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p));
	}
	
	@DeleteMapping(value = "/{platoId}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable(value = "platoId") String platoId) {
		return platoService.listarPorId(platoId)
				.flatMap(p -> {
					return platoService.eliminar(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	// HATEOAS
	@GetMapping(value = "/hateoas/{platoId}")
	public Mono<EntityModel<Plato>> listarHateoasPorId(@PathVariable(value = "platoId") String platoId) {
		Mono<Link> link1 = linkTo(methodOn(PlatoRestController.class).listarHateoasPorId(platoId))
				.withSelfRel().toMono();
		
		Mono<Link> link2 = linkTo(methodOn(PlatoRestController.class).listarHateoasPorId(platoId))
				.withSelfRel().toMono();
		
		//PRACTICA INTERMEDIA
		/*return platoService.listarPorId(platoId)
				.flatMap(p -> {
					return link1.map(link -> {
						return EntityModel.of(p, link);
					});
				});
		*/
		//PRACTICA IDEAL
		/*return platoService.listarPorId(platoId)
				.zipWith(link1, (p, link) -> EntityModel.of(p, link));
		
		*/
		//MULTIPLES LINKS
		/*return link1.zipWith(link2)
				.map(tupla -> {
					Link linkLeft = tupla.getT1();
					Link linkRigth = tupla.getT2();
					return Links.of(linkLeft, linkRigth);
				})
				.zipWith(platoService.listarPorId(platoId), (l, p) -> EntityModel.of(p, l));
		
		*/
		//USANDO LIBRERIA DE HATEOAS PARA MULTIPLES LINKS
		return link1.zipWith(link2)
				.map(function((left, rigth) -> Links.of(left, rigth)))
				.zipWith(platoService.listarPorId(platoId), (links, p) -> EntityModel.of(p, links));
				
	}
	
	@GetMapping(value = "/pageable", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<PageSupport<Plato>>> listarPageable(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return platoService.listarPageable(pageRequest)
				.map(pageSupport -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(pageSupport))
				.defaultIfEmpty(ResponseEntity.notFound()
						.build());
	}
}