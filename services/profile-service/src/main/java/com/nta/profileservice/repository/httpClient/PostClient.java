package com.nta.profileservice.repository.httpClient;

import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.internal.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "post-service")
public interface PostClient {
    @GetMapping("/post/internal/ratings/shipper/{shipperId}")
    ApiResponse<List<RatingResponse>> getRatingsByShipper(@PathVariable String shipperId);
}
