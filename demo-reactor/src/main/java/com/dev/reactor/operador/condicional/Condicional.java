package com.dev.reactor.operador.condicional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Condicional {

	private static final Logger logger = LoggerFactory.getLogger(Condicional.class);
	
	public void defaultIfEmpty() {
		Mono.empty()
			.defaultIfEmpty(new Persona(0, "DEFAULT", 99))
			.subscribe((p) -> {
				logger.info("onNext: defaultIfEmpty -> {}", p.toString());
			});
	}
	
	public void takeUntil() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.takeUntil((p) -> {
				return p.getEdad() > 27;
			})
			.subscribe((p) -> {
				logger.info("onNext: takeUntil -> {}", p.toString());
			});
	}
	
	public void timeout() throws InterruptedException {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			//.delayElements(Duration.ofSeconds(3))
			.delayElements(Duration.ofSeconds(1))
			.timeout(Duration.ofSeconds(2))
			.subscribe((p) -> {
				logger.info("onNext: timeout -> {}", p.toString());
			});
		
		Thread.sleep(10000);
	}
}