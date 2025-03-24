package com.nta.postservice.controller;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.entity.Vehicle;
import com.nta.postservice.service.PostService;
import com.nta.postservice.service.VehicleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleController {
    VehicleService vehicleService;
    PostService postService;

    @GetMapping
    ApiResponse<List<Vehicle>> getAllVehicles() {
        return ApiResponse.<List<Vehicle>>builder().result(vehicleService.findAll()).build();
    }

    @GetMapping("/order-statistic")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Map<String, Long>> countPostByVehicle() {
        return ApiResponse.<Map<String, Long>>builder()
                .result(postService.countOrdersByVehicle())
                .build();
    }
}
