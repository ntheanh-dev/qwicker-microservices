package com.nta.locationservice.controller;

import com.nta.locationservice.dto.response.ApiResponse;
import com.nta.locationservice.dto.response.GoogMapDistanceMatrixApiResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/distance")
public class DistanceController {

    @GetMapping()
    ApiResponse<GoogMapDistanceMatrixApiResponse> getDistance(
            @RequestBody String origin,
            @RequestParam String destination,
            @RequestParam String vehicle,
            @RequestParam String apiKey) {
        return null;
    }
}
