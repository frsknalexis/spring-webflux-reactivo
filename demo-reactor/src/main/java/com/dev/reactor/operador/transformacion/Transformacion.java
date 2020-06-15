package com.dev.reactor.operador.transformacion;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Transformacion {

	private static final Logger logger = LoggerFactory.getLogger(Transformacion.class);
	
	public void methodMap() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.map((p) -> {
				p.setEdad(p.getEdad() + 10);
				return p;
			})
			.subscribe((p) -> {
				logger.info("onNext: map -> {}", p.toString());
			});
		
		Flux.range(0, 9)
			.map((i) -> {
				return i + 10;
			})
			.subscribe((i) -> {
				logger.info("onNext: map2 -> {}", i);
			});
	}
	
	public void methodFlatMap() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.flatMap((p) -> {
				p.setEdad(p.getEdad() + 10);
				return Mono.just(p);
			})
			.subscribe((p) -> {
				logger.info("onNext: flatMap -> {}", p.toString());
			});
	}
	
	public void methodGroupBy() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.groupBy(Persona::getIdPersona, (p) -> {
				return p.getNombres();
			})
			.flatMap((p) -> {
				return p.collectList();
			})
			.subscribe((lista) -> {
				logger.info("onNext: groupBy -> {}", lista.toString());
			});
	}
}