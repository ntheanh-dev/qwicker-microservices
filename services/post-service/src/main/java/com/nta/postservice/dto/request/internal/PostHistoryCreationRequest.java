package com.nta.postservice.dto.request.internal;

import com.nta.postservice.enums.PostHistoryStatus;
import lombok.Getter;

@Getter
public class PostHistoryCreationRequest {
  private String postId;
  private PostHistoryStatus status;
  private String description;
  private String photo;
}
