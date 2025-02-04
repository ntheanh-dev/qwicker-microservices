package com.nta.postservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.postservice.dto.request.PostCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.service.PostService;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @PostMapping
    ApiResponse<Post> createPost(@RequestBody PostCreationRequest request) throws JsonProcessingException {
        var response = postService.createPost(request);
        return ApiResponse.<Post>builder().result(response).build();
    }

    @GetMapping("/{id}")
    ApiResponse<PostResponse> findById(@RequestParam Map<String, String> params, @PathVariable String id) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.findById(params, id))
                .build();
    }

    @PostMapping("/{id}/shipment-accept")
    @PreAuthorize("hasRole('SHIPPER')")
    ApiResponse<?> accept(@PathVariable String id) {
        postService.accept(id);
        return ApiResponse.<PostResponse>builder()
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
