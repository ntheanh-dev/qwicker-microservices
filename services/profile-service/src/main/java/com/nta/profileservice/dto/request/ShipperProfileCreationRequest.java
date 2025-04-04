package com.nta.profileservice.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShipperProfileCreationRequest {
    String accountId;
    String firstName;
    String lastName;

    @NotNull(message = "VEHICLE_NUMBER_REQUIRED")
    String vehicleNumber;

    @NotNull(message = "VEHICLE_ID_REQUIRED")
    String vehicleId;

    @NotNull(message = "AVATAR_BASE64_REQUIRED")
    byte[] avatarBase64;

    @NotNull(message = "IDENTITY_FRONT_BASE64_REQUIRED")
    byte[] identityFBase64;

    @NotNull(message = "IDENTITY_BACK_BASE64_REQUIRED")
    byte[] identityBBase64;
}
