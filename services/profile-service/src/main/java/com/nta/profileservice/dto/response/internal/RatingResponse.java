package com.nta.profileservice.dto.response.internal;

import java.time.LocalDateTime;

import com.nta.profileservice.dto.response.UserProfileResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
