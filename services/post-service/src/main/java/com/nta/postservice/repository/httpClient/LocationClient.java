package com.nta.postservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.internal.LocationCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.DeliveryLocationResponse;

@FeignClient(
        name = "location-service",
        url = "${application.config.location-url}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface LocationClient {
    @PostMapping(value = "/internal/delivery-locations", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DeliveryLocationResponse> createDeliveryLocation(@RequestBody LocationCreationRequest request);

    @GetMapping(value = "/internal/delivery-locations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DeliveryLocationResponse> getDeliveryLocationById(@PathVariable String id);

    @PostMapping(value = "/internal/delivery-locations/find-by-id-list", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<DeliveryLocationResponse>> findByIdList(@RequestBody List<String> idList);
}
