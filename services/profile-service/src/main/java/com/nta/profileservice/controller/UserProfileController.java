package com.nta.profileservice.controller;

import org.springframework.web.bind.annotation.*;

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
public class UserProfileController {
    ProfileService profileService;

    @PostMapping("/users")
    ApiResponse<UserProfileResponse> createUserProfile(@RequestBody final UserProfileCreationRequest request) {
        final var response = profileService.createUserProfile(request);
        return ApiResponse.<UserProfileResponse>builder().result(response).build();
    }

    @GetMapping("/users/{accountId}")
    ApiResponse<UserProfileResponse> getProfileByAccountId(
            @PathVariable("accountId") String accountId,
            @RequestParam(value = "profileType", defaultValue = "DEFAULT") String profileType) {
        final var response = profileService.getUserProfileByAccountIdAndProfileType(accountId, profileType);

        return ApiResponse.<UserProfileResponse>builder().result(response).build();
    }
}
