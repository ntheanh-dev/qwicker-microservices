package com.nta.locationservice.mapper;

import org.mapstruct.Mapper;

import com.nta.locationservice.dto.request.DeliveryLocationCreationRequest;
import com.nta.locationservice.entity.DeliveryLocation;

@Mapper(componentModel = "spring")
public interface DeliveryLocationMapper {
    DeliveryLocation toLocation(DeliveryLocationCreationRequest request);
}
