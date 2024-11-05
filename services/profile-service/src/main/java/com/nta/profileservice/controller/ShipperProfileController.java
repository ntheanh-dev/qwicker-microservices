package com.nta.profileservice.controller;

import org.springframework.web.bind.annotation.*;

import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.ShipperProfileResponse;
import com.nta.profileservice.service.ShipperProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shippers")
public class ShipperProfileController {
    ShipperProfileService shipperProfileService;

    @GetMapping
    ApiResponse<ShipperProfileResponse> getShipperProfile(@RequestParam(value = "accountId") String accountId) {
        final var response = shipperProfileService.getShipperProfile(accountId);
        return ApiResponse.<ShipperProfileResponse>builder().result(response).build();
    }
}
