package com.nta.profileservice.controller;

import com.nta.profileservice.dto.request.ShipperProfileCreationRequest;
import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.ShipperProfileResponse;
import com.nta.profileservice.service.ShipperProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shippers")
public class ShipperProfileController {
    ShipperProfileService shipperProfileService;
    @PostMapping
    ApiResponse<ShipperProfileResponse> createUserProfile(@RequestBody @Valid final ShipperProfileCreationRequest request) {
        final var response = shipperProfileService.createShipperProfile(request);
        return ApiResponse.<ShipperProfileResponse>builder().result(response).build();
    }
}
