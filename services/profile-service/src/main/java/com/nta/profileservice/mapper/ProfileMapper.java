package com.nta.profileservice.mapper;

import com.nta.profileservice.dto.request.ShipperProfileCreationRequest;
import org.mapstruct.Mapper;

import com.nta.profileservice.dto.request.UserProfileCreationRequest;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    UserProfileResponse toUserProfileResponse(Profile profile);

    Profile toUserProfile(UserProfileCreationRequest userProfileCreationRequest);

    Profile toUserProfile(ShipperProfileCreationRequest request);
}
