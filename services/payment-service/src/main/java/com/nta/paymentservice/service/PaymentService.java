package com.nta.paymentservice.service;

import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.entity.Payment;
import com.nta.paymentservice.mapper.PaymentMapper;
import com.nta.paymentservice.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    public Payment createPayment(PaymentCreationRequest payment) {
        return paymentRepository.save(paymentMapper.toPayment(payment));
    }
}
