package com.nta.locationservice.controller.internal;

import com.nta.locationservice.dto.request.DeliveryLocationCreationRequest;
import com.nta.locationservice.dto.response.ApiResponse;
import com.nta.locationservice.entity.DeliveryLocation;
import com.nta.locationservice.service.DeliveryLocationService;
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
@RequestMapping("/internal/delivery-locations")
public class LocationController {
    DeliveryLocationService deliveryLocationService;
    @PostMapping()
    ApiResponse<DeliveryLocation> createLocation(@RequestBody DeliveryLocationCreationRequest deliveryLocation) {
        return ApiResponse.<DeliveryLocation>builder()
                .result(deliveryLocationService.createDeliveryLocation(deliveryLocation))
                .build();
    }
}
