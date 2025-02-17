package com.nta.postservice.controller;

import com.nta.postservice.dto.request.RatingCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.RatingResponse;
import com.nta.postservice.entity.Rating;
import com.nta.postservice.service.RatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
