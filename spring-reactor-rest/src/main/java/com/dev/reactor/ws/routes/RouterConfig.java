package com.dev.reactor.ws.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dev.reactor.ws.handler.PlatoHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> routes(PlatoHandler handler) {
		return route(GET("/v1/platos").or(GET("/v2/platos")), handler::listar)
				.andRoute(GET("/v1/platos/{platoId}"), handler::listarPorId)
				.andRoute(POST("/v1/platos"), handler::registrar)
				.andRoute(PUT("/v1/platos"), handler::modificar)
				.andRoute(DELETE("/v1/platos/{platoId}"), handler::eliminar); 
	}
}