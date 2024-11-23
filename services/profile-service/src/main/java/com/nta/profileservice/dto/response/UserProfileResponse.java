package com.nta.profileservice.dto.response;

import com.nta.profileservice.enums.ProfileType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String accountId;
    String firstName;
    String lastName;
    String email;
    String avatar;
    ProfileType profileType;
}
