package com.nta.identity.dto.response;

import com.nta.identity.entity.AccountStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String id;
    String username;
    String email;
    Set<RoleResponse> roles;
    AccountStatus status;
}
