package com.nta.postservice.dto.request;

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
