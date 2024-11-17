package com.nta.postservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.entity.Vehicle;
import com.nta.postservice.service.VehicleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleController {
    VehicleService vehicleService;

    @GetMapping
    ApiResponse<List<Vehicle>> getAllVehicles() {
        return ApiResponse.<List<Vehicle>>builder()
                .result(vehicleService.findAll())
                .build();
    }
}
