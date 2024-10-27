package com.nta.apigateway.service;

import com.nta.apigateway.dto.request.IntrospectRequest;
import com.nta.apigateway.dto.response.ApiResponse;
import com.nta.apigateway.dto.response.IntrospectResponse;
import com.nta.apigateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(final String token) {
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }
}
