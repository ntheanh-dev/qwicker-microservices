package com.nta.profileservice.dto.request;

import com.nta.profileservice.enums.ProfileType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserProfileByAccountIdRequest {
    private String accountId;
    private ProfileType profileType;
}
