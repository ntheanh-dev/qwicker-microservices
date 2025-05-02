package com.nta.postservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.postservice.dto.request.PostCreationRequest;
import com.nta.postservice.dto.request.UpdatePostStatusRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.dto.response.internal.ShipperProfileResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.service.PostService;
import com.nta.postservice.service.ShipperPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;
    private final ShipperPostService shipperPostService;

    @PostMapping
    ApiResponse<Post> createPost(@RequestBody PostCreationRequest request)
            throws JsonProcessingException {
        var response = postService.createPost(request);
        return ApiResponse.<Post>builder().result(response).build();
    }

    @GetMapping("/{id}")
    ApiResponse<PostResponse> findById(
            @RequestParam Map<String, String> params, @PathVariable String id) {
        return ApiResponse.<PostResponse>builder().result(postService.findById(params, id)).build();
    }

    @GetMapping("/{id}/shippers")
    ApiResponse<ShipperProfileResponse> getShippersByPost(
            @RequestParam String status, @PathVariable String id) {

        return ApiResponse.<ShipperProfileResponse>builder()
                .result(shipperPostService.getShipperProfileByPostId(id, status))
                .build();
    }

    @PostMapping("/{id}/shipment-accept")
    @PreAuthorize("hasRole('SHIPPER')")
    ApiResponse<?> accept(@PathVariable String id) {
        postService.accept(id);
        return ApiResponse.<PostResponse>builder().build();
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

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('SHIPPER')")
    ApiResponse<?> updatePostStatus(
            @PathVariable String id, @RequestBody UpdatePostStatusRequest request) {
        postService.updatePostStatus(
                request.getStatus(), id, request.getPhoto(), request.getDescription());
        return ApiResponse.builder().build();
    }

    @PostMapping("/{id}/cancel")
    ApiResponse<?> cancelPost(@PathVariable String id) {
        postService.cancelOrder(id);
        return ApiResponse.builder().build();
    }


}
