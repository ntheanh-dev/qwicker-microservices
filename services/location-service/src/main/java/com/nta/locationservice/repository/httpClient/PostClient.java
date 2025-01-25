package com.nta.locationservice.repository.httpClient;

import com.nta.locationservice.dto.response.ApiResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "post-service", url = "${application.config.post-url}")
public interface PostClient {
  @GetMapping("/internal/posts/{id}/find-shipper-join")
  ApiResponse<List<String>> findAllJoinedShipperIdsByPostId(
      @PathVariable(name = "id") String postId);

  @PostMapping("/internal/posts/{id}/change-status/{status}")
  ApiResponse<?> changeStatus(
      @PathVariable(name = "id") String postId, @PathVariable(name = "status") String status);
}
