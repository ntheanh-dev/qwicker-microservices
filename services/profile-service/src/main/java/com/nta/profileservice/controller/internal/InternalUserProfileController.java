package com.nta.profileservice.controller.internal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nta.profileservice.dto.request.UserProfileCreationRequest;
import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/users")
public class InternalUserProfileController {
    ProfileService profileService;

    @PostMapping
    ApiResponse<UserProfileResponse> createUserProfile(@RequestBody @Valid final UserProfileCreationRequest request) {
        final var response = profileService.createUserProfile(request);
        return ApiResponse.<UserProfileResponse>builder().result(response).build();
    }
}
