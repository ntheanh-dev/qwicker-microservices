package com.nta.paymentservice.controller;

import com.nta.paymentservice.dto.response.ApiResponse;
import com.nta.paymentservice.dto.response.VNPayResponse;
import com.nta.paymentservice.enums.VNPayStatus;
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
    @PostMapping("/posts/{postId}/collect-cash")
    ApiResponse<?> collectCash(@PathVariable String postId) {
        final String shipperId = authenticationService.getUserDetail().getId();
        paymentService.collectCash(postId, shipperId);
        return ApiResponse.builder().build();
    }

    @GetMapping("/vn-pay")
    public ApiResponse<VNPayResponse> pay(
            final HttpServletRequest request) { // use HttpServletRequest for getting client ip
        return ApiResponse.<VNPayResponse>builder()
                .result(paymentService.createVnPayPayment(request))
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<VNPayStatus> payCallbackHandler(final HttpServletRequest request) {
        final VNPayStatus status = VNPayStatus.fromCode(request.getParameter("vnp_ResponseCode"));
        if (status == VNPayStatus.VNPAY_00) {
            final String postId = request.getParameter("vnp_OrderInfo");
            paymentService.handlePaymentByVNPaySuccess(postId);
        }
        return ApiResponse.<VNPayStatus>builder().result(status).build();
    }
}
