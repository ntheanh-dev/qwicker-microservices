package com.nta.postservice.dto.response.internal;

import java.util.Set;
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
