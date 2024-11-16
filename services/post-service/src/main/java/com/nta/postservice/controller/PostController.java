package com.nta.postservice.controller;

import com.nta.postservice.dto.request.PostCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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
    ApiResponse<PostResponse> findById(
            @RequestParam(value = "status", required = false) String status, @PathVariable String id) {
        PostResponse response;
        if (status != null) {
            response = postService.getPostByStatus(status, id);
        } else {
            response = postService.findById(id);
        }
        return ApiResponse.<PostResponse>builder().result(response).build();
    }
}
