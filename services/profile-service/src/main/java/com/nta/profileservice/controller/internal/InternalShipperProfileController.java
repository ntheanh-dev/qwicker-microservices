package com.nta.profileservice.controller.internal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
