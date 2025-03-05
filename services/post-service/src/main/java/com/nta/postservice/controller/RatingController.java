package com.nta.postservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.nta.postservice.dto.request.RatingCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.RatingResponse;
import com.nta.postservice.entity.Rating;
import com.nta.postservice.service.RatingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {
    RatingService ratingService;

    @PostMapping()
    ApiResponse<Rating> create(@RequestBody RatingCreationRequest request) {
        return ApiResponse.<Rating>builder()
                .result(ratingService.create(request))
                .build();
    }

    @GetMapping("/shipper/{shipperId}")
    public ApiResponse<List<RatingResponse>> getRatingsByShipper(@PathVariable String shipperId) {
        return ApiResponse.<List<RatingResponse>>builder()
                .result(ratingService.getRatingsByShipper(shipperId))
                .build();
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<RatingResponse> getRatingByPostId(@PathVariable String postId) {
        return ApiResponse.<RatingResponse>builder()
                .result(ratingService.getRatingByPostId(postId))
                .build();
    }
}
