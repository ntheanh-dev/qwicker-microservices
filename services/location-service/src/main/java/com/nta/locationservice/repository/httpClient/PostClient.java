package com.nta.locationservice.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nta.locationservice.dto.response.ApiResponse;

@FeignClient(name = "post-service", url = "${application.config.post-url}")
public interface PostClient {
    @GetMapping("/internal/posts/{id}/find-shipper-join")
    ApiResponse<List<String>> findAllJoinedShipperIdsByPostId(@PathVariable(name = "id") String postId);

    @GetMapping("/internal/posts/{id}/change-status")
    ApiResponse<?> changeStatus(@PathVariable(name = "id") String postId, @RequestParam(name = "status") String status);
}
