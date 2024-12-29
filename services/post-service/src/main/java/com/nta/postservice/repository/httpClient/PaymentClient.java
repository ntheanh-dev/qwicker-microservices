package com.nta.postservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.internal.Payment;
import com.nta.postservice.dto.response.ApiResponse;

@FeignClient(
        name = "payment-service",
        url = "${application.config.payment-url}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @PostMapping(value = "/internal/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Payment> createPayment(@RequestBody Payment payment);

    @GetMapping(value = "/internal/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Payment> findByPostId(@RequestParam String postId);

    @GetMapping(value = "/internal/payments/find-by-posts", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<Payment>> findByPostIds(@RequestBody List<String> postIds);
}
