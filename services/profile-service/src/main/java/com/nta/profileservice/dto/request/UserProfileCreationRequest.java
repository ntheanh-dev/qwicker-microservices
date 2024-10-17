package com.nta.profileservice.dto.request;

import com.nta.profileservice.enums.ProfileType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileCreationRequest {
    String firstName;
    String lastName;
    String email;
    ProfileType profileType;
    byte[] photoFile;
}
