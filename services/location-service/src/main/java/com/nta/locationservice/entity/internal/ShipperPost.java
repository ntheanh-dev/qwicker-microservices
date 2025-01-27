package com.nta.locationservice.entity.internal;

import com.nta.locationservice.enums.internal.ShipperPostStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ShipperPost {
  String shipper;
  ShipperPostStatus status;
  LocalDateTime invitedAt;
  LocalDateTime joinedAt;
}
