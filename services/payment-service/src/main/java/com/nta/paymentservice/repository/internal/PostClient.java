package com.nta.paymentservice.repository.internal;

import com.nta.paymentservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "post-service", url = "${application.config.post-url}")
public interface PostClient {
  @GetMapping("/internal/shipper-post")
  ApiResponse<Boolean> isExistByShipperIdAndPostId(
      @RequestParam String shipperId, @RequestParam String postId);

  @GetMapping("/internal/shipper-post/filter")
  ApiResponse<List<String>> getAcceptedPostsByDateRange(@RequestParam("status") String type);
}
