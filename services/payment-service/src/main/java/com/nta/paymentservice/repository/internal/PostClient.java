package com.nta.paymentservice.repository.internal;

import com.nta.paymentservice.configuration.AuthenticationRequestInterceptor;
import com.nta.paymentservice.dto.response.ApiResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "post-service",
        url = "${application.config.post-url}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PostClient {
    @GetMapping("/internal/shipper-post")
    ApiResponse<Boolean> isExistByShipperIdAndPostId(
            @RequestParam String shipperId, @RequestParam String postId);

    @GetMapping("/internal/shipper-post/filter")
    ApiResponse<List<String>> getAcceptedPostsByDateRange(
            @RequestParam("status") String type,
            @RequestParam("timeType") String timeTimes,
            @RequestParam("shipperId") String shipperId,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("endDate") String endDate);

    @PostMapping("/internal/posts/{id}/vn-pay-success")
    ApiResponse<?> vnPaySuccess(@PathVariable(name = "id") String postId);
}
