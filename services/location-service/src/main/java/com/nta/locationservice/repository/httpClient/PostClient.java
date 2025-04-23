package com.nta.locationservice.repository.httpClient;

import com.nta.locationservice.dto.request.internal.PostHistoryCreationRequest;
import com.nta.locationservice.dto.request.internal.ShipperPostCreationRequest;
import com.nta.locationservice.dto.response.ApiResponse;
import com.nta.locationservice.entity.internal.Post;
import com.nta.locationservice.entity.internal.ShipperPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "post-service")
public interface PostClient {
    @PostMapping("/post/internal/posts/{id}/update")
    ApiResponse<?> updatePost(@PathVariable(name = "id") String postId, @RequestBody Post post);

    @PostMapping("/post/internal/shipper-post")
    ApiResponse<?> addShipperPost(@RequestBody ShipperPostCreationRequest request);

    @PostMapping("/post/internal/post-history")
    ApiResponse<?> addPostHistory(@RequestBody PostHistoryCreationRequest request);

    @GetMapping("/post/internal/shipper-post/last/{postId}")
    ApiResponse<ShipperPost> getLastShipperPostByPostId(@PathVariable("postId") String postId);
}
