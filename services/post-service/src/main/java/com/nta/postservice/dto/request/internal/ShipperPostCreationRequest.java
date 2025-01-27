package com.nta.postservice.dto.request.internal;

import lombok.Getter;

@Getter
public class ShipperPostCreationRequest {
  private String postId;
  private String shipperId;
}
