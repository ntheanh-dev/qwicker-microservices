package com.nta.profileservice.controller;

import org.springframework.web.bind.annotation.*;

import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserProfileController {
    UserProfileService userProfileService;

    @GetMapping("/my-profile")
    ApiResponse<UserProfileResponse> getProfileByAccountId(
            @RequestParam(value = "type", defaultValue = "DEFAULT") String profileType) {
        final var response = userProfileService.getMyProfileByType(profileType);

        return ApiResponse.<UserProfileResponse>builder().result(response).build();
    }
}
