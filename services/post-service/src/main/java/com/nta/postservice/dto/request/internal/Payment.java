package com.nta.postservice.dto.request.internal;

import java.math.BigDecimal;

import com.nta.postservice.enums.PaymentMethod;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    BigDecimal price;
    boolean isPosterPay;
    String postId;
    PaymentMethod paymentMethod;
}
