package com.nta.profileservice.dto.response;

import com.nta.profileservice.dto.response.internal.RatingResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
