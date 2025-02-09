package com.nta.profileservice.controller.internal;

import com.nta.profileservice.dto.request.UserProfileCreationRequest;
import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/users")
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping
    ApiResponse<UserProfileResponse> createUserProfile(@RequestBody @Valid final UserProfileCreationRequest request) {
        final var response = userProfileService.createUserProfile(request);
        return ApiResponse.<UserProfileResponse>builder().result(response).build();
    }

    @PostMapping("/batch")
    ApiResponse<Map<String, UserProfileResponse>> getUserProfilesBatch(@RequestBody List<String> accountIds) {
        return ApiResponse.<Map<String, UserProfileResponse>>builder()
                .result(userProfileService.getUserProfilesByIds(accountIds))
                .build();
    }
}
