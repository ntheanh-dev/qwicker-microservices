package com.nta.locationservice.dto.request.internal;

import com.nta.locationservice.enums.internal.PostHistoryStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostHistoryCreationRequest {
  private String postId;
  private PostHistoryStatus status;
  private String description;
  private String photo;
}
