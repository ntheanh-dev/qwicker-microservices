package com.nta.postservice.dto.request;

import com.nta.postservice.dto.request.internal.PaymentCreationRequest;
import com.nta.postservice.dto.request.internal.Shipment;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {
    Shipment shipment;
    ProductCreationRequest product;
    PaymentCreationRequest payment;
    Vehicle order;
}
