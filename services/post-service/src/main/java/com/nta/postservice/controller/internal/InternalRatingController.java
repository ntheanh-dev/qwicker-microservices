package com.nta.postservice.controller.internal;

import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.internal.RatingResponse;
import com.nta.postservice.service.RatingService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalRatingController {
  RatingService ratingService;

  @GetMapping("/shipper/{shipperId}")
  public ApiResponse<List<RatingResponse>> getRatingsByShipper(@PathVariable String shipperId) {
    return ApiResponse.<List<RatingResponse>>builder()
        .result(ratingService.getRatingsByShipper(shipperId))
        .build();
  }
}
