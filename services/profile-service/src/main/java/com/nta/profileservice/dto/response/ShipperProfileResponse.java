package com.nta.profileservice.dto.response;

import java.util.Set;

import com.nta.profileservice.dto.response.internal.RatingResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipperProfileResponse {
    String accountId;
    UserProfileResponse profile;
    String vehicleNumber;
    String vehicleId;
    String identityF;
    String identityB;
    Set<RatingResponse> ratings;
}
