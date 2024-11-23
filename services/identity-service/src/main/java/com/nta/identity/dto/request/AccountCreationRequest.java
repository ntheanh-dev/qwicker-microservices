package com.nta.identity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.nta.identity.enums.AccountType;
import com.nta.identity.enums.ProfileType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

//    @NotNull(message = "AVATAR_BASE64_REQUIRED")
    byte[] avatarBase64;

    @NotNull(message = "PROFILE_TYPE_REQUIRED")
    ProfileType profileType;

    @NotNull(message = "ACCOUNT_TYPE_REQUIRED")
    AccountType accountType;

    // for shipper
    // profile-service will be responsible for validating
    String vehicleNumber;
    String vehicleId;
    byte[] identityFBase64;
    byte[] identityBBase64;
}
