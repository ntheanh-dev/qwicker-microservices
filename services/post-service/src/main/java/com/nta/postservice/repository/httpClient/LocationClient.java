package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.LocationCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.DeliveryLocationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "location-service", url = "${application.config.location-url}", configuration = {AuthenticationRequestInterceptor.class})
public interface LocationClient {
    @PostMapping(value = "/internal/delivery-locations", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DeliveryLocationResponse> createDeliveryLocation(@RequestBody LocationCreationRequest request);

    @GetMapping(value = "/internal/delivery-locations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<DeliveryLocationResponse> getDeliveryLocationById(@PathVariable String id);
}
