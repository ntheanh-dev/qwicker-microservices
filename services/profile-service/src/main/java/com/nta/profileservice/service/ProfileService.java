package com.nta.profileservice.service;

import com.nta.profileservice.enums.ProfileType;
import com.nta.profileservice.exception.AppException;
import com.nta.profileservice.exception.ErrorCode;
import org.springframework.stereotype.Service;

import com.nta.profileservice.dto.request.UserProfileCreationRequest;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.mapper.ProfileMapper;
import com.nta.profileservice.repository.ProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public UserProfileResponse createUserProfile(final UserProfileCreationRequest request) {
        Profile userProfile = profileMapper.toUserProfile(request);
        userProfile.setAccountId("TEST");
        // TODO: Call a upload service to upload avatar

        userProfile = profileRepository.save(userProfile);
        return profileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getUserProfileByAccountIdAndProfileType(
            final String accountId, final String profileType) {
        final var profile = profileRepository
                .findByAccountIdAndProfileType(accountId, ProfileType.valueOf(profileType))
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        return profileMapper.toUserProfileResponse(profile);
    }
}
