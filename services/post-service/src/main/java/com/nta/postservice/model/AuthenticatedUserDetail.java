package com.nta.postservice.model;

import com.nta.postservice.enums.internal.AccountType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedUserDetail {
    private String id;
    private String username;
    private AccountType accountType;
}
