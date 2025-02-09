package com.nta.locationservice.controller;

import com.nta.locationservice.dto.response.ApiResponse;
import com.nta.locationservice.model.ShipperDetailCache;
import com.nta.locationservice.service.GeoHashService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/shipper-location")
public class LocationController {
  GeoHashService geoHashService;

  @GetMapping("/{id}")
  ApiResponse<ShipperDetailCache> currentLocation(@PathVariable String id) {
    return ApiResponse.<ShipperDetailCache>builder()
        .result(geoHashService.getCurrentShipperLocation(id))
        .build();
  }
}
