package com.dev.reactor.operador.filtrado;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;

import reactor.core.publisher.Flux;

public class Filtrado {

	private static final Logger logger = LoggerFactory.getLogger(Filtrado.class);
	
	public void filter() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.filter((p) -> {
				return p.getEdad() > 28;
			})
			.subscribe((p) -> {
				logger.info("onNext: filter -> {}", p.toString());
			});
	}
	
	public void distinct() {
		Flux.fromIterable(List.of(1, 1, 2, 2))
			.distinct()
			.subscribe((i) -> {
				logger.info("onNext: distinct -> {}", i);
			});
		
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(1, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.distinct(Persona::getIdPersona)
			.subscribe((p) -> {
				logger.info("onNext: distinct2 -> {}", p.toString());
			});
	}
	
	public void take() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.take(2)
			.subscribe((p) -> {
				logger.info("onNext: take -> {}", p.toString());
			});
	}
	
	public void takeLast() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.takeLast(2)
			.subscribe((p) -> {
				logger.info("onNext: takeLast -> {}", p.toString());
			});
	}
	
	public void skip() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.skip(2)
			.subscribe((p) -> {
				logger.info("onNext: skip -> {}", p.toString());
			});
	}
	
	public void skipLast() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.skipLast(2)
			.subscribe((p) -> logger.info("onNext: skipLast -> {}", p.toString()));
	}
}