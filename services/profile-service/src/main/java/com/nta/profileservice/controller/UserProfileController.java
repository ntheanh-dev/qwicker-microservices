package com.nta.profileservice.controller;

import org.springframework.web.bind.annotation.*;

import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserProfileController {
    ProfileService profileService;

    @GetMapping
    ApiResponse<UserProfileResponse> getProfileByAccountId(
            @RequestParam(value = "accountId") String accountId,
            @RequestParam(value = "profileType", defaultValue = "DEFAULT") String profileType) {
        final var response = profileService.getUserProfileByAccountIdAndProfileType(accountId, profileType);

        return ApiResponse.<UserProfileResponse>builder().result(response).build();
    }
}
