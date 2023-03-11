package com.rohan.lms.gateway.security;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.rohan.lms.gateway.config.JwtConfig;
import com.rohan.lms.gateway.model.ErrorResponseDto;

import reactor.core.publisher.Flux;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private final RouterValidator routerValidator;
	private final JwtTokenUtil jwtTokenUtil;
	private final JwtConfig jwtConfig;

	public AuthenticationFilter(RouterValidator routerValidator, JwtTokenUtil jwtTokenUtil, JwtConfig config) {
		super(Config.class);
		this.routerValidator = routerValidator;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtConfig = config;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (routerValidator.isSecured.test(exchange.getRequest()) && !jwtConfig.isAuthDisabled()) {
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("Missing Authorisation Header");
				}
			}

			String authHeader = Objects
					.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
			try {
				jwtTokenUtil.validateToken(authHeader);
			} catch (Exception ex) {
				List<String> details = new ArrayList<>();
				details.add(ex.getLocalizedMessage());
				
				ErrorResponseDto error = new ErrorResponseDto(new Date(),
						"UNAUTHORIZED", details, exchange.getRequest().getURI().toString());
				
				ServerHttpResponse response = exchange.getResponse();

				byte[] bytes = SerializationUtils.serialize(error);

				DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				response.writeWith(Flux.just(buffer));
				
//				ServerHttpResponse response = exchange.getResponse();
				
//				return response.setComplete();
				return response.setComplete();
			}

			return chain.filter(exchange);
		});
	}

	public static class Config {

	}
}
