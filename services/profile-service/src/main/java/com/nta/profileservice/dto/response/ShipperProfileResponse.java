package com.nta.profileservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipperProfileResponse {
    UserProfileResponse profile;
    String vehicleNumber;
    String vehicleId;
    String identityF;
    String identityB;
}
