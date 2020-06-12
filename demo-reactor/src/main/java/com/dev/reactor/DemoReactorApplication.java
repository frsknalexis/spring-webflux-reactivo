package com.dev.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dev.reactor.model.Persona;

import io.reactivex.Observable;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DemoReactorApplication.class);
	
	public void reactor() {
		Mono.just(new Persona(1, "Mito", 29))
			.subscribe((p) -> logger.info("onNext: reactor -> {}", p));
	}
	
	public void rxJava2() {
		Observable.just(new Persona(1, "Mito", 29))
				.subscribe((p) -> logger.info("onNext: rxJava -> {}", p));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		reactor();
		rxJava2();
	}
}