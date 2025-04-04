package com.nta.identity.dto.request;

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
    String vehicleNumber;
    String vehicleId;

    @NotNull(message = "AVATAR_BASE64_REQUIRED")
    byte[] avatarBase64;

    @NotNull(message = "IDENTITY_FRONT_BASE64_REQUIRED")
    byte[] identityFBase64;

    @NotNull(message = "IDENTITY_BACK_BASE64_REQUIRED")
    byte[] identityBBase64;
}
