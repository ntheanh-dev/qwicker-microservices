package com.nta.identity.dto.request;

import com.nta.identity.entity.AccountStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeAccountStatusRequest {
    AccountStatus status;
}
