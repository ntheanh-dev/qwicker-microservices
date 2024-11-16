package com.nta.locationservice.mapper;

import com.nta.locationservice.dto.request.DeliveryLocationCreationRequest;
import com.nta.locationservice.entity.DeliveryLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryLocationMapper {
    DeliveryLocation toLocation(DeliveryLocationCreationRequest request);
}
