package com.dev.reactor.operador.combinacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dev.reactor.model.Persona;
import com.dev.reactor.model.Venta;

import reactor.core.publisher.Flux;

public class Combinacion {
	
	private static final Logger logger = LoggerFactory.getLogger(Combinacion.class);
	
	public void merge() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		List<Persona> personas2 = new ArrayList<Persona>();
		personas2.add(new Persona(4, "Harry", 27));
		personas2.add(new Persona(5, "Ron", 28));
		personas2.add(new Persona(6, "Hermione", 29));
		
		List<Venta> ventas = new ArrayList<Venta>();
		ventas.add(new Venta(1, LocalDateTime.now()));
		
		Flux<Persona> fluxPersona = Flux.fromIterable(personas);
		Flux<Persona> fluxPersona2 = Flux.fromIterable(personas2);
		Flux<Venta> fluxVenta = Flux.fromIterable(ventas);
		
		Flux.merge(fluxPersona, fluxPersona2, fluxVenta)
			.subscribe((flux) -> {
				logger.info("onNext: merge -> {}", flux.toString());
			});
	}
	
	public void zip() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		List<Persona> personas2 = new ArrayList<Persona>();
		personas2.add(new Persona(4, "Harry", 27));
		personas2.add(new Persona(5, "Ron", 28));
		personas2.add(new Persona(6, "Hermione", 29));
		
		List<Venta> ventas = new ArrayList<Venta>();
		ventas.add(new Venta(1, LocalDateTime.now()));
		
		Flux<Persona> fluxPersona = Flux.fromIterable(personas);
		Flux<Persona> fluxPersona2 = Flux.fromIterable(personas2);
		Flux<Venta> fluxVenta = Flux.fromIterable(ventas);
		
		Flux.zip(fluxPersona, fluxPersona2, (p1, p2) -> {
				return String.format("Flux1 %s, Flux2 %s", p1.toString(), p2.toString());
			})
			.subscribe((s) -> {
				logger.info("onNext: zip -> {}", s.toString());
			});
		
		Flux.zip(fluxPersona, fluxPersona2, fluxVenta)
			.subscribe((s) -> logger.info("onNext: zip2 -> {}", s.toString()));
		
	}
	
	public void zipWith() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		List<Persona> personas2 = new ArrayList<Persona>();
		personas2.add(new Persona(4, "Harry", 27));
		personas2.add(new Persona(5, "Ron", 28));
		personas2.add(new Persona(6, "Hermione", 29));
		
		List<Venta> ventas = new ArrayList<Venta>();
		ventas.add(new Venta(1, LocalDateTime.now()));
		
		Flux<Persona> fluxPersona = Flux.fromIterable(personas);
		Flux<Persona> fluxPersona2 = Flux.fromIterable(personas2);
		Flux<Venta> fluxVenta = Flux.fromIterable(ventas);
		
		fluxPersona.zipWith(fluxPersona2, (p1, p2) -> {
						return String.format("Flux1 %s, Flux2 %s", p1.toString(), p2.toString());
					})
					.subscribe((s) -> {
						logger.info("onNext: zipWith -> {}", s.toString());
					});
		
		fluxPersona.zipWith(fluxVenta, (p1, v1) -> {
						return String.format("Flux1 %s, Flux2 %s", p1.toString(), v1.toString());
					})
					.subscribe((s) -> {
						logger.info("onNext: zipWith2 -> {}", s.toString());
					});
	}
}