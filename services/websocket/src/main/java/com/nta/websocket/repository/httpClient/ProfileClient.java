package com.nta.websocket.repository.httpClient;

import com.nta.websocket.dto.response.ApiResponse;
import com.nta.websocket.dto.response.internal.ShipperProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${application.config.profile-url}")
public interface ProfileClient {
    @GetMapping("/{id}")
    ApiResponse<ShipperProfileResponse> getShipperProfileByAccountId(@PathVariable("id") final String id);
}
