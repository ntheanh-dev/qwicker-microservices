package com.nta.apigateway.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nta.apigateway.dto.response.ApiResponse;
import com.nta.apigateway.service.IdentityService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${app.api-prefix}")
    private String apiPrefix;

    @NonFinal
    private String[] PUBLIC_ENDPOINTS = new String[] {
        "/identity/auth/.*",
        "/identity/accounts/registration",
        "/identity/accounts/check-username-exists",
        "/identity/accounts/check-email-exists",
        "/post/vehicles",
        "/post/product-category",
    };

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }

        final String token = authHeader.getFirst().replace("Bearer ", "");
        return identityService
                .introspect(token)
                .flatMap(introspectResponseApiResponse -> {
                    if (introspectResponseApiResponse.getResult().isValid()) {
                        return chain.filter(exchange);
                    } else {
                        log.info("invalid token");
                        return unauthenticated(exchange.getResponse());
                    }
                })
                .onErrorResume(throwable -> unauthenticated(exchange.getResponse())); // loi 500, 502...
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(final ServerHttpResponse response) {

        ApiResponse<?> apiResponse =
                ApiResponse.builder().message("Unauthenticated").code(1014).build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    private boolean isPublicEndpoint(final ServerHttpRequest request) {
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }
}
