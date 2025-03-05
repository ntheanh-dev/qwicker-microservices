package com.nta.postservice.controller.internal;

import org.springframework.web.bind.annotation.*;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.entity.Post;
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

    @PostMapping("/{id}/update")
    ApiResponse<?> update(@PathVariable(name = "id") String postId, @RequestBody Post request) {
        postService.update(postId, request);
        return ApiResponse.builder().build();
    }
}
