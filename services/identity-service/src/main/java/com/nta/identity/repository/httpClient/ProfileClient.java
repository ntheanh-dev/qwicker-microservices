package com.nta.identity.repository.httpClient;

import com.nta.identity.dto.request.ShipperProfileCreationRequest;
import com.nta.identity.dto.request.UserProfileCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service")
public interface ProfileClient {
    @PostMapping(value = "/profile/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createUserProfile(@RequestBody UserProfileCreationRequest userProfileCreationRequest);

    @PostMapping(value = "/profile/internal/shippers", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createShipperProfile(@RequestBody ShipperProfileCreationRequest shipperProfileCreationRequest);
}
