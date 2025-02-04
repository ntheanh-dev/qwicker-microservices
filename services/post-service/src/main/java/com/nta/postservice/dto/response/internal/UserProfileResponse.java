package com.nta.postservice.dto.response.internal;

import com.nta.postservice.enums.internal.ProfileType;
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
