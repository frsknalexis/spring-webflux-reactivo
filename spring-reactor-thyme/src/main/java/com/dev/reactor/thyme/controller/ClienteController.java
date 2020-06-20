package com.dev.reactor.thyme.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.dev.reactor.thyme.document.Cliente;
import com.dev.reactor.thyme.service.ClienteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/clientes")
@SessionAttributes("cliente")
public class ClienteController {

	private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
	
	private final ClienteService clienteService;
	
	@Value("${ruta.subida}")
	private String RUTA_SUBIDA;
	
	public ClienteController(@Autowired @Qualifier("clienteService") ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@GetMapping("/listar")
	public Mono<String> listar(Model model) {
		Flux<Cliente> clientesFlux = clienteService.listar();
		model.addAttribute("clientes", clientesFlux);
		return Mono.just("clientes/listar");
	}
	
	@GetMapping("/form")
	public Mono<String> crear(Model model) {
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("titulo", "Formulario de Cliente");
		model.addAttribute("boton", "Crear");
		return Mono.just("clientes/form");
	}
	
	@GetMapping("/form/{id}")
	public Mono<String> editar(@PathVariable(value = "id") String id, Model model) {
		Mono<Cliente> clienteMono = clienteService.listarPorId(id)
												.doOnNext((c) -> logger.info("Cliente: " + c.getNombres()))
												.defaultIfEmpty(new Cliente());
		
		model.addAttribute("titulo", "Editar Cliente");
		model.addAttribute("boton", "Editar");
		model.addAttribute("cliente", clienteMono);
		return Mono.just("clientes/form");
	}
	
	@PostMapping("/operar")
	public Mono<String> operar(@Valid Cliente cliente, BindingResult validation, SessionStatus status, Model model,
								@RequestPart FilePart file) {
		if (validation.hasErrors()) {
			validation.reject("ERR780", "Errores de validaciones de formulario");
			model.addAttribute("titulo", "Errores en el formulario cliente");
			return Mono.just("clientes/form");
		}
		status.setComplete();
		if (!file.filename().isEmpty()) {
			cliente.setUrlFoto(file.filename());
		}
		return clienteService.registrar(cliente)
							.flatMap((c) -> {
								if (!file.filename().isEmpty()) {
									return file.transferTo(new File(RUTA_SUBIDA + c.getUrlFoto()));
								}
								return Mono.empty();
							})
							.then(Mono.just("redirect:/clientes/listar?success=cliente+guardado"));
	}
	
	@GetMapping("/eliminar/{id}")
	public Mono<String> eliminar(@PathVariable(value = "id") String id) {
		return clienteService.listarPorId(id)
							.flatMap((c) -> {
								return clienteService.eliminar(c.getId())
												.then(Mono.just("redirect:/clientes/listar?success=cliente+eliminado"));
							})
							.defaultIfEmpty("redirect:/clientes/listar?error=cliente+no+encontrado")
							.onErrorResume((ex) -> Mono.just("redirect:/clientes/listar?error=Error+Interno"))
							.onErrorReturn("redirect:/clientes/listar?error=Error+Interno");
	}
	
	@GetMapping("/subidas/img/{nombreFoto:.+}")
	public Mono<ResponseEntity<Resource>> verFoto(@PathVariable(value = "nombreFoto") String nombreFoto) throws MalformedURLException {
		Path path = Paths.get(RUTA_SUBIDA).resolve(nombreFoto).toAbsolutePath();
		Resource imagen = new UrlResource(path.toUri());
		return Mono.just(ResponseEntity.ok()
									.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagen.getFilename() + "\"")
									.body(imagen));
	}
	
	//CONTRAPRESION
	@GetMapping("/listar/datadriver")
	public Mono<String> listarDataDriver(Model model) {
		final int CANTIDAD_ELEMENTOS_MOSTRAR  = 1;
		Flux<Cliente> fluxClientes = clienteService.listarDemorado();
		model.addAttribute("clientes", new ReactiveDataDriverContextVariable(fluxClientes, 
										CANTIDAD_ELEMENTOS_MOSTRAR));
		model.addAttribute("titulo", "Listado De Clientes [Data-Driver]");
		return Mono.just("clientes/listar");
	}
	
	@GetMapping("/listar/full")
	public Mono<String> listarFull(Model model) {
		Flux<Cliente> fluxClientes = clienteService.listarSobrecargado();
		model.addAttribute("clientes", fluxClientes);
		model.addAttribute("titulo", "Listado De Clientes [Full]");
		return Mono.just("clientes/listar");
	}
	
	@GetMapping("/listar/frag")
	public Mono<String> listarFragmentado(Model model) {
		Flux<Cliente> clientesFlux = clienteService.listarSobrecargado();
		model.addAttribute("clientes", clientesFlux);
		model.addAttribute("titulo", "Listado De Clientes [Full - Fragmentado]");
		return Mono.just("clientes/listar-chunked");
	}
}