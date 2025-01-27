package com.nta.locationservice.dto.request.internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipperPostCreationRequest {
  private String postId;
  private String shipperId;
}
