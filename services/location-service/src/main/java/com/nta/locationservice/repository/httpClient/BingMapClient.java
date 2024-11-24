package com.nta.locationservice.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nta.locationservice.dto.response.DurationBingMapApiResponse;

@FeignClient(name = "bingMap", url = "https://dev.virtualearth.net/REST/v1/Routes/Driving")
public interface BingMapClient {
    @GetMapping()
    DurationBingMapApiResponse getBingMap(
            @RequestParam(name = "o", defaultValue = "json") String o,
            @RequestParam(name = "wp.0") String wp0,
            @RequestParam(name = "wp.1") String wp1,
            @RequestParam(name = "key") String key);
}
