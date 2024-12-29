package com.nta.postservice.dto.request.internal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nta.postservice.enums.DeliveryTimeType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shipment {
    BigDecimal cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-M-d H:m")
    LocalDateTime pickupDatetime;

    DeliveryTimeType deliveryTimeType; // now or latter
    LocationCreationRequest pickupLocation;
    LocationCreationRequest dropLocation;
}
