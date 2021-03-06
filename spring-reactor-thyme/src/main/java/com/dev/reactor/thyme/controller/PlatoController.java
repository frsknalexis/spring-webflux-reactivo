package com.dev.reactor.thyme.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.dev.reactor.thyme.document.Plato;
import com.dev.reactor.thyme.service.PlatoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/platos")
@SessionAttributes("plato")
public class PlatoController {

	private final PlatoService platoService;
	
	public PlatoController(@Autowired @Qualifier("platoService") PlatoService platoService) {
		this.platoService = platoService;
	}
	
	@GetMapping("/listar")
	public Mono<String> listar(Model model) {
		Flux<Plato> platosFlux = platoService.listar();
		model.addAttribute("platos", platosFlux);
		return Mono.just("platos/listar");
	}
	
	@GetMapping("/form")
	public Mono<String> irNuevo(Model model) {
		model.addAttribute("plato", new Plato());
		model.addAttribute("titulo", "Formulario de Plato");
		return Mono.just("platos/form");
	}
	
	@GetMapping("/form/{id}")
	public Mono<String> irEditar(@PathVariable(value = "id") String id, Model model) {
		Mono<Plato> platoMono = platoService.listarPorId(id)
											.defaultIfEmpty(new Plato());
		model.addAttribute("plato", platoMono);
		model.addAttribute("titulo", "Editar Plato");
		return Mono.just("platos/form");
	}
	
	@PostMapping("/operar")
	public Mono<String> operar(@Valid Plato plato, BindingResult validations, SessionStatus status, Model model) {
		if (validations.hasErrors()) {
			validations.reject("ERR780", "Errores de validaciones de formulario");
			model.addAttribute("titulo", "Errores en el formulario producto");
			return Mono.just("platos/form");
		}
		status.setComplete();
		return platoService.registrar(plato)
						.then(Mono.just("redirect:/platos/listar?success=plato+guardado"));
	}
	
	@GetMapping("/eliminar/{id}")
	public Mono<String> eliminar(@PathVariable(value = "id") String id) {
		return platoService.listarPorId(id)
						.flatMap((p) -> {
							return platoService.eliminar(p.getId())
											.then(Mono.just("redirect:/platos/listar?success=plato+eliminado"));
						})
						.defaultIfEmpty("redirect:/platos/listar?error=plato+no+encontrado")
						.onErrorResume((ex) -> Mono.just("redirect:/platos/listar?error=Error+Interno"))
						.onErrorReturn("redirect:/platos/listar?error=Error+Interno");
		
	}
	
	//Metodo utilizado para JQuery para la parte de autocomplete
	@GetMapping(value = "/cargarplatos/{term}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Flux<Plato> buscarPorNombre(@PathVariable(value = "term") String term) {
		return platoService.buscarPorNombre(term);
	}
}