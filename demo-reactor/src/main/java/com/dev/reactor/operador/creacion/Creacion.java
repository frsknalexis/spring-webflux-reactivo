package com.dev.reactor.operador.creacion;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;

import io.reactivex.Observable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Creacion {

	private static final Logger logger = LoggerFactory.getLogger(Creacion.class);
	
	public void justFrom() {
		Mono.just(new Persona(1, "Mito", 29));
	}
	
	public void empty() {
		Mono.empty();
		Flux.empty();
		Observable.empty();
	}
	
	public void range() {
		Flux.range(0, 3)
			.doOnNext((i) -> {
				logger.info("doOnNext: i -> {}", i);
			})
			.subscribe();
	}
	
	public void repeat() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.repeat(3)
			.subscribe((p) -> {
				logger.info("onNext Flux: repeat -> {}", p.toString());
			});
		
		Mono.just(new Persona(1, "Mito", 29))
			.repeat(3)
			.subscribe((p) -> logger.info("onNext Mono: repeat -> {}", p));
	}
}