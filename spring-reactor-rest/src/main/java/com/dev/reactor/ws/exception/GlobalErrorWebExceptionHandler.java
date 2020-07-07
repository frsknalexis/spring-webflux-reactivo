package com.dev.reactor.ws.exception;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.server.RequestPredicates.all;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
			ApplicationContext context, ServerCodecConfigurer configurer) {
		super(errorAttributes, resourceProperties, context);
		this.setMessageWriters(configurer.getWriters());
	}
	
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(all(), this::renderErrorResponse);
	}
	
	@SuppressWarnings("deprecation")
	private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Map<String, Object> errorDefaultMap = getErrorAttributes(request, false);
		Map<String, Object> mapException = new HashMap<String, Object>();
		
		var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String status = String.valueOf(errorDefaultMap.get("status"));
		
		switch (status) {
		case "500":
			mapException.put("error", "500");
			mapException.put("excepcion", "Error Interno");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			break;
		case "400":
			mapException.put("error", "400");
			mapException.put("excepcion", "Bad Request");
			httpStatus = HttpStatus.BAD_REQUEST;
		default:
			break;
		}
		
		mapException.put("mensaje", errorDefaultMap.get("message"));
		mapException.put("ruta", request.uri());
		mapException.put("fecha", LocalDateTime.now());
		
		return ServerResponse.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(mapException));
	}
}