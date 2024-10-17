package com.nta.profileservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileCreationRequest {
    String firstName;
    String lastName;
    String email;
    byte[] photoFile;
}
