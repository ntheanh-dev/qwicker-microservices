package com.nta.profileservice.dto.request;

import com.nta.profileservice.enums.ProfileType;

import jakarta.validation.constraints.NotNull;
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
    String avatarBase64;
    ProfileType profileType;
}
