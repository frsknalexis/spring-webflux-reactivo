package com.dev.reactor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dev.reactor.model.Persona;
import com.dev.reactor.operador.combinacion.Combinacion;
import com.dev.reactor.operador.condicional.Condicional;
import com.dev.reactor.operador.creacion.Creacion;
import com.dev.reactor.operador.error.ErrorOperador;
import com.dev.reactor.operador.filtrado.Filtrado;
import com.dev.reactor.operador.matematico.Matematico;
import com.dev.reactor.operador.transformacion.Transformacion;

import io.reactivex.Observable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DemoReactorApplication.class);
	
	public void reactor() {
		Mono.just(new Persona(1, "Mito", 29))
			.doOnNext((p) -> logger.info("doOnNext: Reactor -> {}", p))
			.subscribe((p) -> logger.info("onNext: Reactor -> {}", p));
	}
	
	public void rxJava2() {
		Observable.just(new Persona(1, "Mito", 29))
				.doOnNext((p) -> {
					logger.info("doOnNext: RxJava -> {}", p);
				})
				.subscribe((p) -> logger.info("onNext: RxJava -> {}", p));
	}
	
	public void crearMono() {
		Mono.just(new Persona(1, "Mito", 29))
			.subscribe((p) -> { 
				logger.info("onNext: Mono -> {}", p.toString());
			});
	}
	
	public void crearFlux() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.subscribe((p) -> {
				logger.info("onNext: Flux -> {}", p.toString());
			});
	}
	
	public void fluxToMono() {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona(1, "Mito", 27));
		personas.add(new Persona(2, "Code", 28));
		personas.add(new Persona(3, "Jaime", 29));
		
		Flux.fromIterable(personas)
			.collectList()
			.subscribe((lista) -> {
				logger.info("onNext -> {}", lista.toString());
			});
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		reactor();
		rxJava2();
		crearMono();
		crearFlux();
		fluxToMono();
		
		Creacion creacion = new Creacion();
		creacion.range();
		creacion.repeat();
		
		Transformacion transformacion = new Transformacion();
		transformacion.methodMap();
		transformacion.methodFlatMap();
		transformacion.methodGroupBy();
		
		Filtrado filtrado = new Filtrado();
		filtrado.filter();
		filtrado.distinct();
		filtrado.take();
		filtrado.takeLast();
		filtrado.skip();
		filtrado.skipLast();
		
		Combinacion combinacion = new Combinacion();
		combinacion.merge();
		combinacion.zip();
		combinacion.zipWith();
		
		ErrorOperador error = new ErrorOperador();
		//error.retry();
		error.onErrorReturn();
		error.onErrorResume();
		//error.onErrorMap();
		
		Condicional condicional = new Condicional();
		condicional.defaultIfEmpty();
		condicional.takeUntil();
		condicional.timeout();
		
		Matematico matematico = new Matematico();
		matematico.average();
		matematico.count();
		matematico.min();
		matematico.sum();
		matematico.summarizing();
	}
}