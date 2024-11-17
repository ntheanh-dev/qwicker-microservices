package com.nta.paymentservice.controller.internal;

import org.springframework.web.bind.annotation.*;

import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.dto.response.ApiResponse;
import com.nta.paymentservice.entity.Payment;
import com.nta.paymentservice.service.PaymentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/payments")
@Slf4j
public class PaymentController {
    PaymentService paymentService;

    @PostMapping()
    ApiResponse<Payment> createPayment(@RequestBody PaymentCreationRequest payment) {
        return ApiResponse.<Payment>builder()
                .result(paymentService.createPayment(payment))
                .build();
    }

    @GetMapping
    ApiResponse<Payment> findByPostId(@RequestParam(value = "postId", required = false) String postId) {
        return ApiResponse.<Payment>builder()
                .result(paymentService.findByPostId(postId))
                .build();
    }
}
