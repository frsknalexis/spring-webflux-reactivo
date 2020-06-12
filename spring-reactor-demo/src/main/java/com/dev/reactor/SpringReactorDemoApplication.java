package com.dev.reactor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringReactorDemoApplication.class);
	
	private static List<String> platos = Arrays.asList("Hamburguesa", "Pizza");
	
	public static void main(String[] args) {
		SpringApplication.run(SpringReactorDemoApplication.class, args);
	}
	
	public void crearMono() {
		Mono<Integer> mono = Mono.just(7);
		mono.subscribe((x) -> logger.info("onNext -> {}", x));
		
		mono.subscribe((x) -> logger.info("Numero -> {}", x));
		
		Flux.fromIterable(platos)
			.collectList()
			.flatMapMany((lista) -> Flux.fromIterable(lista))
			.subscribe((s) -> logger.info("onNext: flatMapMany -> {}", s));
	}
	
	public void crearFlux() {
		Flux.fromIterable(platos)
			.subscribe((s) -> logger.info("onNext Plato -> {}", s));
	}
	
	public void methodDoOnNext() {
		Flux.fromIterable(platos)
			.doOnNext((p) -> logger.info("plato -> {}", p))
			.subscribe((p) -> logger.info("onNext -> {}", p));
	}
	
	public void methodMap() {
		Flux.fromIterable(platos)
			.map((p) -> "Plato: " + p)
			.subscribe((p) -> logger.info(p));
	}
	
	public void methodFlatMap() {
		Mono.just("Jaime")
			.flatMap((i) -> Mono.just(i))
			.map((s) -> s.length())
			.subscribe((i) -> logger.info("onNext: Rpta -> {}", i));
		
		Mono.just("Alexis")
			.flatMap((s) -> Mono.just("Nombre: " + s))
			.subscribe((s) -> logger.info("onNext: flatMap -> {}", s));
	}
	
	public void methodRange() {
		Flux.range(0, 10)
			.map((i) -> i + 1)
			.subscribe((i) -> logger.info("onNext: N -> {}", i));
	}
	
	public void methodDelayElement() throws InterruptedException {
		Flux.range(0, 10)
			.delayElements(Duration.ofSeconds(2))
			.doOnNext((i) -> logger.info(i.toString()))
			.subscribe();
		
		Thread.sleep(30000);
	}

	public void methodZipWith() {
		Flux<String> fluxPlatos = Flux.fromIterable(platos);
		List<String> clientes = Arrays.asList("Jaime", "Code");
		Flux<String> fluxClientes = Flux.fromIterable(clientes);
		fluxPlatos.zipWith(fluxClientes, (p, c) -> String.format("Flux1 %s Flux2 %s", p, c))
				.subscribe((s) -> logger.info("onNext: zipWith -> {}", s));
	}
	
	public void methodMergeWith() {
		Flux<String> fluxPlatos = Flux.fromIterable(platos);
		List<String> clientes = Arrays.asList("Jaime", "Code");
		Flux<String> fluxClientes = Flux.fromIterable(clientes);
		
		fluxPlatos.mergeWith(fluxClientes)
				.subscribe((s) -> logger.info("onNext: mergeWith -> {}", s));
	}
	
	public void methodMerge() {
		Flux<String> fluxPlatos = Flux.fromIterable(platos);
		List<String> clientes = Arrays.asList("Jaime", "Code");
		Flux<String> fluxClientes = Flux.fromIterable(clientes);
		
		Flux.merge(fluxPlatos, fluxClientes)
			.subscribe((s) -> logger.info("onNext: merge -> {}", s));
	}
	
	public void methodFilter() {
		Flux.fromIterable(platos)
			.filter((s) -> s.startsWith("H"))
			.subscribe((s) -> logger.info("onNext: filter -> {}", s));
	}
	
	public void methodTakeLast() {
		Flux.fromIterable(platos)
			.takeLast(1)
			.subscribe((s) -> logger.info("onNext: takeLast -> {}", s));
	}
	
	public void methodTake() {
		Flux.fromIterable(platos)
			.take(1)
			.subscribe((s) -> logger.info("onNext: take -> {}", s));
	}
	
	public void methodDefaultIfEmpty() {
		platos = new ArrayList<String>();
		Flux.fromIterable(platos)
			.defaultIfEmpty("VACIO")
			.subscribe((s) -> logger.info("onNext: defaultIfEmpty -> {}", s));
	}
	
	public void methodOnErrorReturn() {
		Flux.fromIterable(platos)
			.doOnNext((s) -> {
				throw new ArithmeticException("MAL CALCULO");
			})
			.onErrorMap((ex) -> new ArithmeticException(ex.getMessage()))
			.onErrorReturn("OCURRIO UN ERROR")
			.subscribe((s) -> logger.info("onNext -> {}", s));
	}
	
	public void methodRetry() {
		Flux.fromIterable(platos)
			.doOnNext((p) -> {
				logger.info("intentando... {}", p);
				throw new ArithmeticException("MAL CALCULO");
			})
			.retry(3)
			.onErrorReturn("OCURRIO UN ERROR")
			.subscribe((ex) -> logger.info(ex));
	}
	
	@Override
	public void run(String... args) throws Exception {
		crearMono();
		crearFlux();
		methodDoOnNext();
		methodMap();
		methodFlatMap();
		methodRange();
		//methodDelayElement();
		methodZipWith();
		methodMergeWith();
		methodMerge();
		methodFilter();
		methodTakeLast();
		methodTake();
		//methodDefaultIfEmpty();
		methodOnErrorReturn();
		methodRetry();
	}
}