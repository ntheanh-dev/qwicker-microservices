package com.nta.profileservice.controller.internal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.nta.profileservice.dto.request.ShipperProfileCreationRequest;
import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.ShipperProfileResponse;
import com.nta.profileservice.service.ShipperProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/shippers")
public class InternalShipperProfileController {
    ShipperProfileService shipperProfileService;

    @PostMapping
    ApiResponse<ShipperProfileResponse> createShipperProfile(
            @RequestBody @Valid final ShipperProfileCreationRequest request) {
        final var response = shipperProfileService.createShipperProfile(request);
        return ApiResponse.<ShipperProfileResponse>builder().result(response).build();
    }

    @GetMapping("/{id}")
    ApiResponse<ShipperProfileResponse> getShipperProfileByAccountId(@PathVariable("id") final String id) {
        return ApiResponse.<ShipperProfileResponse>builder()
                .result(shipperProfileService.getShipperProfileById(id))
                .build();
    }
}
