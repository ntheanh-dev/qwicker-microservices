package com.nta.paymentservice.mapper;

import org.mapstruct.Mapper;

import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentCreationRequest paymentCreationRequest);
}
