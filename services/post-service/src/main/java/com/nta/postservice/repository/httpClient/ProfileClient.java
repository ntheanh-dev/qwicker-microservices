package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.ShipperProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "profile-service",
    url = "${application.config.profile-url}",
    configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
  @GetMapping(value = "/internal/shippers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ApiResponse<ShipperProfileResponse> getShipperProfileByAccountId(
      @PathVariable("id") final String id);
}
