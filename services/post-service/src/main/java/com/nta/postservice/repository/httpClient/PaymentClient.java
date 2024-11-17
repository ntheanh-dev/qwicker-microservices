package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.Payment;
import com.nta.postservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", url = "${application.config.payment-url}", configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @PostMapping(value = "/internal/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createPayment(@RequestBody Payment payment);

    @GetMapping(value = "/internal/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Payment> findByPostId(@RequestParam String postId);
}
