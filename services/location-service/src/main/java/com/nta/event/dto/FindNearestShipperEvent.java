package com.nta.event.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash
public class FindNearestShipperEvent implements Serializable {
  @Serial private static final long serialVersionUID = 7156526077883281625L;

  String postId;
  Double latitude;
  Double longitude;
  String vehicleId;
  int km;
  String postResponse;
  LocalDateTime timestamp;
}
