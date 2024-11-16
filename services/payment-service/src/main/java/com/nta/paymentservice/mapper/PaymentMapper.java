package com.nta.paymentservice.mapper;

import com.nta.paymentservice.dto.request.PaymentCreationRequest;
import com.nta.paymentservice.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentCreationRequest paymentCreationRequest);
}
