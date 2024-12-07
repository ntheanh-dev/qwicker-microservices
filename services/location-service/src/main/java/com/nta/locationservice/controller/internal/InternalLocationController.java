package com.nta.locationservice.controller.internal;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.nta.locationservice.dto.request.DeliveryLocationCreationRequest;
import com.nta.locationservice.dto.response.ApiResponse;
import com.nta.locationservice.entity.DeliveryLocation;
import com.nta.locationservice.service.DeliveryLocationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/delivery-locations")
public class InternalLocationController {
    DeliveryLocationService deliveryLocationService;

    @PostMapping()
    ApiResponse<DeliveryLocation> createLocation(@RequestBody DeliveryLocationCreationRequest deliveryLocation) {
        return ApiResponse.<DeliveryLocation>builder()
                .result(deliveryLocationService.createDeliveryLocation(deliveryLocation))
                .build();
    }

    @PostMapping("/find-by-id-list")
    ApiResponse<List<DeliveryLocation>> findByIdList(@RequestBody List<String> idList) {
        return ApiResponse.<List<DeliveryLocation>>builder()
                .result(deliveryLocationService.findAllByIdList(idList))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<DeliveryLocation> findById(@PathVariable String id) {
        return ApiResponse.<DeliveryLocation>builder()
                .result(deliveryLocationService.getDeliveryLocationById(id))
                .build();
    }
}
