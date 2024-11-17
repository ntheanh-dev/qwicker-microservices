package com.nta.paymentservice.dto.request;

import java.math.BigDecimal;

import com.nta.paymentservice.enums.PaymentMethod;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreationRequest {
    BigDecimal price;
    boolean isPosterPay;
    String postId;
    PaymentMethod paymentMethod;
}
