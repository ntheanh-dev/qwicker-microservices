package com.nta.locationservice.dto.request.internal;

import com.nta.locationservice.enums.internal.AccountStatus;

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
