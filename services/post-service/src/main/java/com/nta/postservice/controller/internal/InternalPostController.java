package com.nta.postservice.controller.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.enums.PostStatus;
import com.nta.postservice.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/update")
    ApiResponse<?> update(@PathVariable(name = "id") String postId, @RequestBody Post request) {
        postService.update(postId, request);
        return ApiResponse.builder().build();
    }

    @PostMapping("/{id}/vn-pay-success")
    ApiResponse<?> vnPaySuccess(@PathVariable(name = "id") String postId)
            throws JsonProcessingException {
        postService.handleVNPaySuccess(postId);
        return ApiResponse.builder().build();
    }
}
