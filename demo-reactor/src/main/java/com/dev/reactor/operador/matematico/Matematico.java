package com.dev.reactor.operador.matematico;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;

import reactor.core.publisher.Flux;

public class Matematico {

	private static final Logger logger = LoggerFactory.getLogger(Matematico.class);
	
	public void average() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.collect(Collectors.averagingInt(Persona::getEdad))
			.subscribe((p) -> {
				logger.info("onNext: average -> {}", p);
			});
	}
	
	public void count() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.count()
			.subscribe((c) -> {
				logger.info("onNext: count -> {}", c);
			});
	}
	
	public void min() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.collect(Collectors.minBy(Comparator.comparing(Persona::getEdad)))
			.subscribe((p) -> {
				logger.info("onNext: minBy -> {}", p.get().toString());
			});
	}
	
	public void sum() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.collect(Collectors.summingInt(Persona::getEdad))
			.subscribe((s) -> {
				logger.info("onNext: sum -> {}", s);
			});
	}
	
	public void summarizing() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.collect(Collectors.summarizingInt(Persona::getEdad))
			.subscribe((s) ->  {
				logger.info("onNext: summarizing -> {}", s);
			});
	}
}