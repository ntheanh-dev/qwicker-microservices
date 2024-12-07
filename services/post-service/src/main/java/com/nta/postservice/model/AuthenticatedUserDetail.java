package com.nta.postservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedUserDetail {
    private String id;
    private String username;
}
