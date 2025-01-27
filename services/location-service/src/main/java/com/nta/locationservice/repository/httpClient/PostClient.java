package com.nta.locationservice.repository.httpClient;

import com.nta.locationservice.dto.request.internal.PostHistoryCreationRequest;
import com.nta.locationservice.dto.request.internal.ShipperPostCreationRequest;
import com.nta.locationservice.dto.response.ApiResponse;
import com.nta.locationservice.entity.internal.Post;
import com.nta.locationservice.entity.internal.ShipperPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "post-service", url = "${application.config.post-url}")
public interface PostClient {
  @PostMapping("/internal/posts/{id}/update")
  ApiResponse<?> updatePost(@PathVariable(name = "id") String postId, @RequestBody Post post);

  @PostMapping("/internal/shipper-post")
  ApiResponse<?> addShipperPost(@RequestBody ShipperPostCreationRequest request);

  @PostMapping("/internal/post-history")
  ApiResponse<?> addPostHistory(@RequestBody PostHistoryCreationRequest request);

  @GetMapping("/internal/shipper-post/last/{postId}")
  ApiResponse<ShipperPost> getLastShipperPostByPostId(@PathVariable("postId") String postId);
}
