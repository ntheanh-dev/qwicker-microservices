package com.nta.postservice.dto.response;

import com.nta.postservice.entity.PostHistory;
import com.nta.postservice.entity.Product;
import com.nta.postservice.enums.DeliveryTimeType;
import com.nta.postservice.enums.PostStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

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

//    String pickupLocationId;
    LocalDateTime pickupDatetime;

//    String dropLocationId;
    LocalDateTime dropDateTime;

//    String userId;

//    String vehicleId;
    Set<PostHistory> history;
}
