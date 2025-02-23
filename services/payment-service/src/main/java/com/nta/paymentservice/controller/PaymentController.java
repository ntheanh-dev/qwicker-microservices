package com.nta.paymentservice.controller;

import com.nta.paymentservice.dto.response.ApiResponse;
import com.nta.paymentservice.dto.response.VNPayResponse;
import com.nta.paymentservice.service.AuthenticationService;
import com.nta.paymentservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/payments")
@Slf4j
public class PaymentController {
    PaymentService paymentService;
    AuthenticationService authenticationService;

    @PreAuthorize("hasRole('SHIPPER')")
    @PostMapping("/collect-cash")
    ApiResponse<?> collectCash(@RequestParam(value = "postId", required = true) String postId) {
        final String shipperId = authenticationService.getUserDetail().getId();
        paymentService.collectCash(shipperId, postId);
        return ApiResponse.builder().build();
    }

    @GetMapping("/vn-pay")
    public ApiResponse<VNPayResponse> pay(
            final HttpServletRequest request) { // use HttpServletRequest for getting client ip
        return ApiResponse.<VNPayResponse>builder()
                .result(paymentService.createVnPayPayment(request))
                .build();
    }

}
