package com.nta.postservice.controller;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.CountNumPostResponse;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {

    PostService postService;

    @GetMapping("/total")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<CountNumPostResponse> countNumPosts() {
        return ApiResponse.<CountNumPostResponse>builder()
                .result(postService.countNumPosts())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<PostResponse>> getAllPosts(
            @RequestParam(value = "status", required = false) String statusList) {
        List<PostResponse> response;
        try {
            response = postService.getPostsByStatusListForAdmin(statusList);
        } catch (IllegalArgumentException e) {
            response = List.of();
        }

        return ApiResponse.<List<PostResponse>>builder().result(response).build();
    }

}
