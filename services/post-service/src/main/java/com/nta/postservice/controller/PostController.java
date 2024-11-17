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

import java.util.Map;

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
        return ApiResponse.<PostResponse>builder().result(postService.findById(params, id)).build();
    }
}
