package com.nta.websocket.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nta.websocket.dto.response.ApiResponse;
import com.nta.websocket.model.internal.PostStatus;

@FeignClient(name = "post-service", url = "${application.config.post-url}")
public interface PostClient {
  //    @GetMapping("internal/posts/{id}/find-status")
  //    ApiResponse<PostStatus> findStatusByPostId(@PathVariable String id);
  //
  //    @GetMapping("internal/posts/{id}/count-shipper-join")
  //    ApiResponse<Integer> countShipperJoinedByPostId(@PathVariable(name = "id") String id);
  //
  //    @PostMapping("/{post-id}/shipper-join/{shipper-id}")
  //    ApiResponse<?> shipperJoinPost(
  //            @PathVariable(name = "post-id") String postId, @PathVariable(name = "shipper-id")
  // String shipperId);
  //
  //    @GetMapping("/{post-id}/is-shipper-join/{shipper-id}")
  //    ApiResponse<Boolean> isShipperJoinPost(
  //            @PathVariable(name = "post-id") String postId, @PathVariable(name = "shipper-id")
  // String shipperId);
  //
  //    @GetMapping("/{id}/find-shipper-join")
  //    ApiResponse<List<String>> findAllJoinedShipperIdsByPostId(@PathVariable(name = "id") String
  // postId);
}
