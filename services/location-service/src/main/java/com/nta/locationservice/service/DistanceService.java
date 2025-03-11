package com.nta.locationservice.service;

import com.nta.locationservice.dto.response.GoogMapDistanceMatrixApiResponse;
import com.nta.locationservice.repository.httpClient.GoogMapClient;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerProperties;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DistanceService {

    GoogMapClient googleMapClient;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final CircuitBreakerProperties circuitBreakerProperties;

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleGetDistanceFallback")
    public GoogMapDistanceMatrixApiResponse getDistanceMatrix(
            final String origin,
            final String destination,
            final String vehicleId,
            final String apiKey) {

        return googleMapClient.getDistance(origin, destination, vehicleId, apiKey);
    }

    protected GoogMapDistanceMatrixApiResponse handleGetDistanceFallback(Throwable throwable)
            throws Throwable {
        // TODO handle Logic
        return null;
    }
}
