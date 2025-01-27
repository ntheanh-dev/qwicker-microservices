package com.nta.locationservice.entity.internal;

import com.nta.locationservice.enums.internal.PostStatus;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Post {
  PostStatus status;
}
