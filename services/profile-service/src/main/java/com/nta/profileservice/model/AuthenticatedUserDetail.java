package com.nta.profileservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedUserDetail {
    private String username;
    private String id;
}
