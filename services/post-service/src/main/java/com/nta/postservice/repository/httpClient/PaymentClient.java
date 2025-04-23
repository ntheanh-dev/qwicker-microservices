package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.internal.Payment;
import com.nta.postservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "payment-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @PostMapping(value = "/payment/internal/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Payment> createPayment(@RequestBody Payment payment);

    @GetMapping(value = "/payment/internal/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Payment> findByPostId(@RequestParam String postId);

    @GetMapping(value = "/payment/internal/payments/find-by-posts", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<Payment>> findByPostIds(@RequestBody List<String> postIds);
}
