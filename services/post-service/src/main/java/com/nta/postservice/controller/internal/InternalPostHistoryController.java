package com.nta.postservice.controller.internal;

import com.nta.postservice.dto.request.internal.PostHistoryCreationRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.service.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/post-history")
@RequiredArgsConstructor
public class InternalPostHistoryController {
  private final PostHistoryService postHistoryService;

  @PostMapping()
  ApiResponse<?> addPostHistory(@RequestBody PostHistoryCreationRequest request) {
    postHistoryService.save(request);
    return ApiResponse.builder().build();
  }
}
