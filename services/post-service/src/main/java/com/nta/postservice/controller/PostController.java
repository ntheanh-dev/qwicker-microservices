package com.nta.postservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.nta.postservice.dto.request.PostCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.service.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @PostMapping
    ApiResponse<Post> createPost(@RequestBody PostCreationRequest request) {
        var response = postService.createPost(request);
        return ApiResponse.<Post>builder().result(response).build();
    }

    @GetMapping("/{id}")
    ApiResponse<PostResponse> findById(@RequestParam Map<String, String> params, @PathVariable String id) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.findById(params, id))
                .build();
    }

    @GetMapping
    ApiResponse<List<PostResponse>> getAllUserPosts(
            @RequestParam(value = "status", required = false) String statusList) {
        List<PostResponse> response;
        try {
            response = postService.getPostsByStatusList(statusList);
        } catch (IllegalArgumentException e) {
            response = List.of();
        }

        return ApiResponse.<List<PostResponse>>builder().result(response).build();
    }
}
