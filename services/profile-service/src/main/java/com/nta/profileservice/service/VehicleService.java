package com.nta.profileservice.service;

import com.nta.profileservice.entity.Vehicle;
import com.nta.profileservice.exception.AppException;
import com.nta.profileservice.enums.ErrorCode;
import com.nta.profileservice.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleService {
    VehicleRepository vehicleRepository;
    public Vehicle findVehicleById(final String id) {
        return vehicleRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
    }
}
