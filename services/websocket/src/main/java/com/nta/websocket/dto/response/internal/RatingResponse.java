package com.nta.websocket.dto.response.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponse {
    double rating;
    String feedback;
    LocalDateTime createdAt;
    UserProfileResponse user;
}
