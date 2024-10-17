package com.nta.profileservice.dto.response;

import com.nta.profileservice.entity.Vehicle;

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
    Vehicle vehicle;
    String identityF;
    String identityB;
}
