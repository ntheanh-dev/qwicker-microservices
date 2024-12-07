package com.nta.paymentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.entity.Payment;
import com.nta.paymentservice.enums.ErrorCode;
import com.nta.paymentservice.exception.AppException;
import com.nta.paymentservice.mapper.PaymentMapper;
import com.nta.paymentservice.repository.PaymentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;

    public Payment createPayment(final PaymentCreationRequest payment) {
        return paymentRepository.save(paymentMapper.toPayment(payment));
    }

    public Payment findByPostId(final String postId) {
        return paymentRepository.findByPostId(postId).orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    public List<Payment> findByPostIds(final List<String> postIds) {
        return paymentRepository.findByPostIds(postIds);
    }
}
