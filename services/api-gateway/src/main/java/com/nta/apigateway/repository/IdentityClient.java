package com.nta.apigateway.repository;

import java.awt.*;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.nta.apigateway.dto.request.IntrospectRequest;
import com.nta.apigateway.dto.response.ApiResponse;
import com.nta.apigateway.dto.response.IntrospectResponse;

import reactor.core.publisher.Mono;

@Repository
public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
