package com.nta.profileservice.service;

import org.springframework.stereotype.Service;

import com.nta.profileservice.dto.request.UploadImageRequest;
import com.nta.profileservice.dto.request.UserProfileCreationRequest;
import com.nta.profileservice.dto.response.UploadImageResponse;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.enums.ErrorCode;
import com.nta.profileservice.enums.ProfileType;
import com.nta.profileservice.exception.AppException;
import com.nta.profileservice.mapper.ProfileMapper;
import com.nta.profileservice.repository.ProfileRepository;
import com.nta.profileservice.repository.httpClient.FileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    FileClient fileClient;

    public UserProfileResponse createUserProfile(final UserProfileCreationRequest request) {
        Profile userProfile = profileMapper.toUserProfile(request);
        // Call file-service to upload avatar
//        final UploadImageResponse uploadImageResponse = fileClient.uploadImage(UploadImageRequest.builder()
//                .isMultiple(false)
//                .base64(request.getAvatarBase64())
//                .build());
//        userProfile.setAvatar(uploadImageResponse.getUrl());
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

    public Profile findByAccountId(final String accountId) {
        return profileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
    }
}
