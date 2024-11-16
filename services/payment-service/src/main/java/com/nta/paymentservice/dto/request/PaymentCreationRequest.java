package com.nta.paymentservice.dto.request;

import com.nta.paymentservice.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
