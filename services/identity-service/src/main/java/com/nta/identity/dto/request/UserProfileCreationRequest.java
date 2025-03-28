package com.nta.identity.dto.request;

import jakarta.validation.constraints.NotNull;

import com.nta.identity.enums.ProfileType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileCreationRequest {
    String accountId;
    String firstName;
    String lastName;

    @NotNull(message = "AVATAR_BASE64_REQUIRED")
    byte[] avatarBase64;

    ProfileType profileType;
}
