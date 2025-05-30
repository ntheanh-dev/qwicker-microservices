package com.nta.profileservice.service;

import com.nta.profileservice.dto.request.ShipperProfileCreationRequest;
import com.nta.profileservice.dto.response.ShipperProfileResponse;
import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.entity.ShipperProfile;
import com.nta.profileservice.enums.ErrorCode;
import com.nta.profileservice.enums.ProfileType;
import com.nta.profileservice.exception.AppException;
import com.nta.profileservice.mapper.ProfileMapper;
import com.nta.profileservice.mapper.ShipperProfileMapper;
import com.nta.profileservice.model.AuthenticatedUserDetail;
import com.nta.profileservice.repository.ProfileRepository;
import com.nta.profileservice.repository.ShipperProfileRepository;
import com.nta.profileservice.repository.httpClient.PostClient;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipperProfileService {
  ShipperProfileRepository shipperProfileRepository;
  ProfileMapper profileMapper;
  ProfileRepository profileRepository;
  UserProfileService userProfileService;
  ShipperProfileMapper shipperProfileMapper;
  AuthenticationService authenticationService;
  PostClient postClient;

  @Transactional
  public ShipperProfileResponse createShipperProfile(final ShipperProfileCreationRequest request) {
    // TODO: Call to another service to upload images and receive urls
    final List<byte[]> base64 =
        List.of(
            request.getAvatarBase64(), request.getIdentityFBase64(), request.getIdentityFBase64());
    final List<String> ulrs =
        List.of("http:example1.com", "http:example2.com", "http:example3.com");

    // Create profile record
    Profile profile = profileMapper.toUserProfile(request);
    profile.setProfileType(ProfileType.DEFAULT);
    profile.setAvatar(ulrs.getFirst());
    profile = profileRepository.save(profile);

    // Create shipper profile
    ShipperProfile shipperProfile = shipperProfileMapper.toShipperProfile(request);
    shipperProfile.setProfile(profile);
    shipperProfile.setIdentityF(ulrs.get(1));
    shipperProfile.setIdentityB(ulrs.get(2));
    shipperProfile = shipperProfileRepository.save(shipperProfile);
    return shipperProfileMapper.toShipperProfileResponse(shipperProfile);
  }

  public ShipperProfileResponse getShipperProfile() {
    final AuthenticatedUserDetail shipper =
        authenticationService.getAuthenticatedUserDetailFromToken();
    final Profile profile = userProfileService.findByAccountId(shipper.getId());
    final ShipperProfile shipperProfile =
        shipperProfileRepository
            .findByProfileId(profile.getId())
            .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

    final ShipperProfileResponse shipperProfileResponse =
        shipperProfileMapper.toShipperProfileResponse(shipperProfile);
    shipperProfileResponse.setProfile(profileMapper.toUserProfileResponse(profile));
    shipperProfileResponse.setAccountId(shipper.getId());
    return shipperProfileResponse;
  }

  public List<ShipperProfileResponse> getAllShipperProfiles() {
    return shipperProfileRepository.findAll().stream()
        .map(shipperProfileMapper::toShipperProfileResponse)
        .toList();
  }

  public ShipperProfileResponse getShipperProfileById(final String id) {
    final Profile profile = userProfileService.findByAccountId(id);
    final ShipperProfile shipperProfile =
        shipperProfileRepository
            .findByProfileId(profile.getId())
            .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

    final var ratings = postClient.getRatingsByShipper(profile.getId()).getResult();

    final ShipperProfileResponse shipperProfileResponse =
        shipperProfileMapper.toShipperProfileResponse(shipperProfile);
    shipperProfileResponse.setProfile(profileMapper.toUserProfileResponse(profile));
    shipperProfileResponse.setAccountId(id);
    shipperProfileResponse.setRatings(ratings);
    return shipperProfileResponse;
  }
}
