package com.nta.postservice.controller.internal;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.enums.PostStatus;
import com.nta.postservice.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/posts")
@RequiredArgsConstructor
public class InternalPostController {
    private final PostService postService;

    @GetMapping("/{id}/find-status")
    ApiResponse<PostStatus> findPostStatusByPostId(@PathVariable String id) {
        return ApiResponse.<PostStatus>builder()
                .result(postService.findPostStatusByPostId(id))
                .build();
    }

    @PostMapping("/{post-id}/shipper-join/{shipper-id}")
    ApiResponse<?> shipperJoinPost(
            @PathVariable(name = "post-id") String postId, @PathVariable(name = "shipper-id") String shipperId) {
        postService.shipperJoinPost(postId, shipperId);
        return ApiResponse.builder().build();
    }

    @GetMapping("/{post-id}/is-shipper-join/{shipper-id}")
    ApiResponse<Boolean> isShipperJoinPost(
            @PathVariable(name = "post-id") String postId, @PathVariable(name = "shipper-id") String shipperId) {
        postService.shipperJoinPost(postId, shipperId);
        return ApiResponse.<Boolean>builder().build();
    }

    @GetMapping("/{id}/count-shipper-join")
    ApiResponse<Integer> countShipperJoinedByPostId(@PathVariable(name = "id") String postId) {
        return ApiResponse.<Integer>builder()
                .result(postService.countShipperJoinByPostId(postId))
                .build();
    }

    @GetMapping("/{id}/find-shipper-join")
    ApiResponse<List<String>> findAllJoinedShipperIdsByPostId(@PathVariable(name = "id") String postId) {
        return ApiResponse.<List<String>>builder()
                .result(postService.findAllJoinedShipperIdsByPostId(postId))
                .build();
    }

    @GetMapping("/{id}/change-status")
    ApiResponse<?> changeStatus(
            @PathVariable(name = "id") String postId, @RequestParam(name = "status") String status) {
        postService.changeStatus(postId, status);
        return ApiResponse.builder().build();
    }
}
