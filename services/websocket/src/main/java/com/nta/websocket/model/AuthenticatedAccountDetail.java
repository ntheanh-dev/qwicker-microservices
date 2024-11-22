package com.nta.websocket.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedAccountDetail {
    private String username;
    private String id;
}
