package com.nta.identity.mapper;

import org.mapstruct.Mapper;

import com.nta.identity.dto.request.AccountCreationRequest;
import com.nta.identity.dto.request.ShipperProfileCreationRequest;
import com.nta.identity.dto.request.UserProfileCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    UserProfileCreationRequest toUserProfileCreationRequest(AccountCreationRequest accountCreationRequest);

    ShipperProfileCreationRequest toShipperProfileCreationRequest(AccountCreationRequest accountCreationRequest);
}
