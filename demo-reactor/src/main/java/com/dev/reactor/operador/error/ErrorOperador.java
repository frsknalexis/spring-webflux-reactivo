package com.dev.reactor.operador.error;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ErrorOperador {

	private static final Logger logger = LoggerFactory.getLogger(ErrorOperador.class);
	
	public void retry() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.concatWith(Flux.error( new RuntimeException("OCURRIO UN ERROR")))
			.retry(1)
			.doOnNext((i) -> logger.info("onNext: retry -> {}", i.toString()))
			.subscribe();
	}
	
	public void onErrorReturn() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.concatWith(Flux.error(() -> {
				return new RuntimeException("OCURRIO UN ERROR");
			}))
			.onErrorReturn(new Persona(0, "XYZ", 99))
			.subscribe((x) -> {
				logger.info("onNext: onErrorReturn -> {}", x.toString());
			});
	}
	
	public void onErrorResume() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.concatWith(Flux.error(() -> {
				return new RuntimeException("OCURRIO UN ERROR");
			}))
			.onErrorResume((ex) -> Mono.just(new Persona(0, "XYZ", 99)))
			.subscribe((x) -> {
				logger.info("onNext: onErrorResume -> {}", x.toString());
			});
	}
	
	public void onErrorMap() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.concatWith(Flux.error(() -> {
				return new RuntimeException("OCURRIO UN ERROR");
			}))
			.onErrorMap((ex) -> {
				return new InterruptedException(ex.getMessage());
			})
			.subscribe((s) -> {
				logger.info("onNext: onErrorMap -> {}", s.toString());
			});
	}
}