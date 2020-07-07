package com.dev.reactor.ws.controller;

import java.io.File;
import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.dev.reactor.ws.document.Cliente;
import com.dev.reactor.ws.pagination.PageSupport;
import com.dev.reactor.ws.service.ClienteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
public class ClienteRestController {

	private final ClienteService clienteService;
	
	@Value("${ruta.subida}")
	private String RUTA_SUBIDA;
	
	public ClienteRestController(@Autowired @Qualifier("clienteService") ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Flux<Cliente>>> listar() {
		Flux<Cliente> clientesFlux = clienteService.listar();
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientesFlux));
	}
	
	@GetMapping(value = "/{clienteId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Cliente>> listarPorId(@PathVariable(value = "clienteId") String clienteId) {
		return clienteService.listarPorId(clienteId)
				.map(c -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(c))
				.defaultIfEmpty(ResponseEntity.notFound()
						.build());
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Cliente>> registrar(@Valid @RequestBody Cliente cliente, ServerHttpRequest request) {
		return clienteService.registrar(cliente)
				.map(c -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/")
						.concat(c.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(c));
	}
	
	@PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<Cliente>> modificar(@Valid @RequestBody Cliente cliente, ServerHttpRequest request) {
		return clienteService.modificar(cliente)
				.map(c -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/")
						.concat(c.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(c));
	}
	
	@DeleteMapping(value = "/{clienteId}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable(value = "clienteId") String clienteId) {
		return clienteService.listarPorId(clienteId)
				.flatMap(c -> {
					return clienteService.eliminar(c.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	//HATEOAS
	@GetMapping(value = "/hateoas/{clienteId}")
	public Mono<EntityModel<Cliente>> listarHateoasPorId(@PathVariable(value = "clienteId") String clienteId) {
		Mono<Link> link1 = linkTo(methodOn(ClienteRestController.class).listarHateoasPorId(clienteId))
				.withSelfRel().toMono();
		Mono<Link> link2 = linkTo(methodOn(ClienteRestController.class).listar()).withRel("clientes")
				.toMono();
		
		return link1.zipWith(link2, (linkLeft, linkRigth) -> Links.of(linkLeft, linkRigth))
				.zipWith(clienteService.listarPorId(clienteId), (links, c) -> EntityModel.of(c, links));
	}
	
	@GetMapping(value = "/pageable", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<PageSupport<Cliente>>> listarPaginado(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return clienteService.listarPageable(pageRequest)
				.map(pageSupport -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(pageSupport))
				.defaultIfEmpty(ResponseEntity.notFound()
						.build());
	}
	
	// SUBIR FOTO
	@PostMapping(value = "/foto/{clienteId}")
	public Mono<ResponseEntity<Cliente>> subirFoto(@PathVariable(value = "clienteId") String clienteId,
			@RequestPart FilePart file) {
		return clienteService.listarPorId(clienteId)
				.flatMap(c -> {
					if (!file.filename().isEmpty()) {
						c.setUrlFoto(file.filename());
						return file.transferTo(new File(RUTA_SUBIDA + c.getUrlFoto()))
								.then(clienteService.registrar(c));
					}
					return Mono.empty();
				})
				.map(c -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(c))
				.defaultIfEmpty(ResponseEntity.notFound()
						.build());
	}
}