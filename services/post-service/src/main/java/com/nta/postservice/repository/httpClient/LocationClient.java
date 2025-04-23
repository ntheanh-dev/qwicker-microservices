package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.internal.LocationCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.DeliveryLocationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "location-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface LocationClient {
    @PostMapping(value = "/location/internal/delivery-locations", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DeliveryLocationResponse> createDeliveryLocation(@RequestBody LocationCreationRequest request);

    @GetMapping(value = "/location/internal/delivery-locations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DeliveryLocationResponse> getDeliveryLocationById(@PathVariable String id);

    @PostMapping(value = "/location/internal/delivery-locations/find-by-id-list", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<DeliveryLocationResponse>> findByIdList(@RequestBody List<String> idList);
}
