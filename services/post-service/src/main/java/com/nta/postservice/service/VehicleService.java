package com.nta.postservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nta.postservice.entity.Vehicle;
import com.nta.postservice.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}
