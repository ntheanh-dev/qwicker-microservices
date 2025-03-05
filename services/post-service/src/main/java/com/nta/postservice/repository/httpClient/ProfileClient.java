package com.nta.postservice.repository.httpClient;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.ShipperProfileResponse;
import com.nta.postservice.dto.response.internal.UserProfileResponse;

@FeignClient(
        name = "profile-service",
        url = "${application.config.profile-url}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping(value = "/internal/shippers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ShipperProfileResponse> getShipperProfileByAccountId(@PathVariable("id") final String id);

    @PostMapping("/internal/users/batch")
    ApiResponse<Map<String, UserProfileResponse>> getUsersByBatch(@RequestBody final List<String> raterIds);
}
