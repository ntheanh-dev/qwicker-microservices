package com.nta.postservice.dto.request.internal;

import com.nta.postservice.enums.PaymentMethod;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreationRequest {
    boolean isPosterPay;
    PaymentMethod method;
}
