package com.nta.profileservice.mapper;

import org.mapstruct.Mapper;

import com.nta.profileservice.dto.request.ShipperProfileCreationRequest;
import com.nta.profileservice.dto.response.ShipperProfileResponse;
import com.nta.profileservice.entity.ShipperProfile;

@Mapper(componentModel = "spring")
public interface ShipperProfileMapper {
    ShipperProfile toShipperProfile(ShipperProfileCreationRequest shipperProfile);

    ShipperProfileResponse toShipperProfileResponse(ShipperProfile shipperProfile);
}
