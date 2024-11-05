package com.nta.identity.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nta.identity.dto.request.ShipperProfileCreationRequest;
import com.nta.identity.dto.request.UserProfileCreationRequest;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profile")
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createUserProfile(@RequestBody UserProfileCreationRequest userProfileCreationRequest);

    @PostMapping(value = "/internal/shippers", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createShipperProfile(@RequestBody ShipperProfileCreationRequest shipperProfileCreationRequest);
}
