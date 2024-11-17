package com.nta.locationservice.service;

import com.nta.locationservice.dto.request.DeliveryLocationCreationRequest;
import com.nta.locationservice.entity.DeliveryLocation;
import com.nta.locationservice.enums.ErrorCode;
import com.nta.locationservice.exception.AppException;
import com.nta.locationservice.mapper.DeliveryLocationMapper;
import com.nta.locationservice.repository.DeliveryLocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DeliveryLocationService {
    DeliveryLocationMapper deliveryLocationMapper;
    DeliveryLocationRepository deliveryLocationRepository;

    public DeliveryLocation createDeliveryLocation(final DeliveryLocationCreationRequest request) {
        final DeliveryLocation deliveryLocation = deliveryLocationMapper.toLocation(request);
        return deliveryLocationRepository.save(deliveryLocation);
    }

    public DeliveryLocation getDeliveryLocationById(final String id) {
        return deliveryLocationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));
    }
}
