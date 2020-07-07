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

import com.dev.reactor.ws.document.Factura;
import com.dev.reactor.ws.pagination.PageSupport;
import com.dev.reactor.ws.service.ClienteService;
import com.dev.reactor.ws.service.FacturaService;
import com.dev.reactor.ws.service.PlatoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static reactor.function.TupleUtils.function;

@RestController
@RequestMapping("/facturas")
public class FacturaRestController {

	private final FacturaService facturaService;
	
	private final ClienteService clienteService;
	
	private final PlatoService platoService;
	
	public FacturaRestController(@Autowired @Qualifier("facturaService") FacturaService facturaService,
			@Autowired @Qualifier("clienteService") ClienteService clienteService,
			@Autowired @Qualifier("platoService") PlatoService platoService) {
		this.facturaService = facturaService;
		this.clienteService = clienteService;
		this.platoService = platoService;
	}
	
	private Mono<Factura> getFacturaDetalle(Factura factura) {
		return Mono.just(factura)
				// OBTENER DETALLE CLIENTE
				.zipWith(clienteService.listarPorId(factura.getCliente()
						.getId()), (f, c) -> {
							f.setCliente(c);
							return f;
							})
				// OBTENER DETALLE PLATO
				.flatMapMany(f -> {
					return Flux.fromIterable(f.getItems())
							.flatMap(item -> {
								return Mono.just(item)
										.zipWith(platoService.listarPorId(item.getPlato()
												.getId()), (i, p) -> {
													i.setPlato(p);
													return i;
												});
							});
				})
				.collectList()
				.flatMap(listaItems -> {
					factura.setItems(listaItems);
					return Mono.just(factura);
				});
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Flux<Factura>>> listar() {
		Flux<Factura> facturasFlux = facturaService.listar()
				.flatMap(factura -> {
					return getFacturaDetalle(factura);
				});
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(facturasFlux));
	}
	
	@GetMapping(value = "/{facturaId}")
	public Mono<ResponseEntity<Factura>> listarPorId(@PathVariable(value = "facturaId") String facturaId) {
		return facturaService.listarPorId(facturaId)
				.flatMap(factura -> getFacturaDetalle(factura))
				.map(f -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(f))
				.defaultIfEmpty(ResponseEntity.notFound()
						.build());
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Factura>> registrar(@Valid @RequestBody Factura factura, ServerHttpRequest request) {
		return facturaService.registrar(factura)
				.flatMap(f -> getFacturaDetalle(f))
				.map(f -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/")
						.concat(f.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(f));
	}
	
	@PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Factura>> modificar(@Valid @RequestBody Factura factura, ServerHttpRequest request) {
		return facturaService.modificar(factura)
				.flatMap(f -> getFacturaDetalle(f))
				.map(f -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/")
						.concat(f.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(f));
	}
	
	@DeleteMapping(value = "/{facturaId}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable(value = "facturaId") String facturaId) {
		return facturaService.listarPorId(facturaId)
				.flatMap(f -> {
					return facturaService.eliminar(f.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	// HATEOAS
	@GetMapping(value = "/hateoas/{facturaId}")
	public Mono<EntityModel<Factura>> listarHateoasPorId(@PathVariable(value = "facturaId") String facturaId) {
		Mono<Link> link1 = linkTo(methodOn(FacturaRestController.class).listarPorId(facturaId))
				.withSelfRel().toMono();
		Mono<Link> link2 = linkTo(methodOn(FacturaRestController.class).listar()).withRel("facturas")
				.toMono();
		
		return link1.zipWith(link2)
				.map(function((left, rigth) -> Links.of(left, rigth)))
				.zipWith(facturaService.listarPorId(facturaId)
						.flatMap(factura -> getFacturaDetalle(factura)), 
						(links, f) -> EntityModel.of(f, links));
	}
	
	@GetMapping(value = "/pageable", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<PageSupport<Factura>>> listarPaginado(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return facturaService.listarPageable(pageRequest)
				.zipWith(facturaService.listar()
						.flatMap(f -> getFacturaDetalle(f))
						.skip(page * size)
						.take(size).collectList(), (pageSupport, list) -> {
							pageSupport.setContent(list);
							return pageSupport;
						})
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p));
	}
}