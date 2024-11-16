package com.nta.postservice.dto.request;

import com.nta.postservice.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
