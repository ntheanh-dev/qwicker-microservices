package com.nta.websocket.dto.response.internal;

import com.nta.websocket.enums.internal.ProfileType;

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
