package com.nta.locationservice.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nta.locationservice.dto.response.GoogMapDistanceMatrixApiResponse;

@FeignClient(name = "googMap", url = "https://rsapi.goong.io")
public interface GoogMapClient {
    @GetMapping("/DistanceMatrix")
    public GoogMapDistanceMatrixApiResponse getDistance(
            @RequestParam(name = "origins") String origins,
            @RequestParam(name = "destinations") String destinations,
            @RequestParam(name = "vehicle") String vehicle,
            @RequestParam(name = "api_key") String key);
}
