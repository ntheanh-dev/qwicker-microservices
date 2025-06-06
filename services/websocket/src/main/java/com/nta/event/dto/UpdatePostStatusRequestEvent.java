package com.nta.event.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePostStatusRequestEvent implements Serializable {
  @Serial private static final long serialVersionUID = 7156526077883281625L;
  PostMessageType postMessageType;
  String postId;
}
