package com.nta.postservice.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.nta.postservice.dto.request.Payment;
import com.nta.postservice.entity.PostHistory;
import com.nta.postservice.entity.Product;
import com.nta.postservice.enums.DeliveryTimeType;
import com.nta.postservice.enums.PostStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String description;
    LocalDateTime postTime;
    DeliveryTimeType deliveryTimeType;
    PostStatus status;
    Product product;

    DeliveryLocationResponse pickupLocation;
    LocalDateTime pickupDatetime;

    DeliveryLocationResponse dropLocation;
    LocalDateTime dropDateTime;

    Payment payment;
    //    String userId;

    //    String vehicleId;
    Set<PostHistory> history;
}
