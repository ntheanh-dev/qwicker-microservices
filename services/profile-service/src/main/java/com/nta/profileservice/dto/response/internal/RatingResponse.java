package com.nta.profileservice.dto.response.internal;

import com.nta.profileservice.dto.response.UserProfileResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponse {
    String raterId;
    String shipperId;
    String postId;
    double rating;
    String feedback;
    LocalDateTime createdAt;
    UserProfileResponse user;
}
