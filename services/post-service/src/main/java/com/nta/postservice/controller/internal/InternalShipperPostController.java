package com.nta.postservice.controller.internal;

import java.text.ParseException;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.nta.postservice.dto.request.internal.ShipperPostCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.service.ShipperPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/shipper-post")
@RequiredArgsConstructor
public class InternalShipperPostController {

    private final ShipperPostService shipperPostService;

    @PostMapping("")
    ApiResponse<?> addShipperPost(@RequestBody ShipperPostCreationRequest request) {
        shipperPostService.add(request.getPostId(), request.getShipperId());
        return ApiResponse.builder().build();
    }

    @GetMapping("/last/{postId}")
    ApiResponse<ShipperPost> getLastShipperPostByPostId(@PathVariable("postId") String postId) {
        return ApiResponse.<ShipperPost>builder()
                .result(shipperPostService.getLastShipperPostByPostId(postId))
                .build();
    }

    @GetMapping("")
    ApiResponse<Boolean> isExistByShipperIdAndPostId(@RequestParam String shipperId, @RequestParam String postId) {
        return ApiResponse.<Boolean>builder()
                .result(shipperPostService.isShipperPostExist(postId, shipperId))
                .build();
    }

    @GetMapping("/filter")
    ApiResponse<List<String>> getAcceptedPostsByShipperId(
            @RequestParam("status") String type,
            @RequestParam("timeType") String timeType,
            @RequestParam("shipperId") String shipperId,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("endDate") String endDate)
            throws ParseException {
        return ApiResponse.<List<String>>builder()
                .result(shipperPostService.findPostIdsByDateRange(type, timeType, shipperId, fromDate, endDate))
                .build();
    }
}
