package com.nta.profileservice.service;

import com.nta.profileservice.dto.request.UserProfileCreationRequest;
import com.nta.profileservice.dto.response.UserProfileResponse;
import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.enums.ErrorCode;
import com.nta.profileservice.enums.ProfileType;
import com.nta.profileservice.exception.AppException;
import com.nta.profileservice.mapper.ProfileMapper;
import com.nta.profileservice.model.AuthenticatedUserDetail;
import com.nta.profileservice.repository.ProfileRepository;
import com.nta.profileservice.repository.httpClient.FileClient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    FileClient fileClient;
    AuthenticationService authenticationService;

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

    public UserProfileResponse getMyProfileByType(final String profileType) {
        final AuthenticatedUserDetail user = authenticationService.getAuthenticatedUserDetailFromToken();
        final var profile = profileRepository
                .findByAccountIdAndProfileType(user.getId(), ProfileType.valueOf(profileType))
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        final var profileResponse = profileMapper.toUserProfileResponse(profile);
        profileResponse.setAccountId(user.getId());
        return profileResponse;
    }

    public Profile findByAccountId(final String accountId) {
        return profileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
    }

    public Map<String, UserProfileResponse> getUserProfilesByIds(final List<String> userIds) {
        final List<Profile> userProfiles = profileRepository.findByAccountIdIn(userIds);
        return userProfiles.stream().collect(Collectors.toMap(Profile::getAccountId, profileMapper::toUserProfileResponse));
    }
}
