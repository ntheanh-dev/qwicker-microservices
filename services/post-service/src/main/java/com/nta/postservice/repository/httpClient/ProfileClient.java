package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.ShipperProfileResponse;
import com.nta.postservice.dto.response.internal.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "profile-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping(value = "/profile/internal/shippers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ShipperProfileResponse> getShipperProfileByAccountId(@PathVariable("id") final String id);

    @PostMapping("/profile/internal/users/batch")
    ApiResponse<Map<String, UserProfileResponse>> getUsersByBatch(@RequestBody final List<String> raterIds);
}
