package com.dev.reactor.ws.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class RequestValidator {

	@Autowired
	private  Validator validator;
	
	public <T> Mono<T> validate(T t) {
		if (t == null) {
			return Mono.error(new IllegalArgumentException());
		}
		
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		
		if (violations == null || violations.isEmpty()) {
			return Mono.just(t);
		}
		return Mono.error(new ConstraintViolationException(violations));
	}
}