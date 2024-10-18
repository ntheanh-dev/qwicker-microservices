package com.nta.profileservice.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.nta.profileservice.dto.request.ShipperProfileCreationRequest;
import com.nta.profileservice.dto.response.ShipperProfileResponse;
import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.entity.ShipperProfile;
import com.nta.profileservice.entity.Vehicle;
import com.nta.profileservice.enums.ErrorCode;
import com.nta.profileservice.enums.ProfileType;
import com.nta.profileservice.exception.AppException;
import com.nta.profileservice.mapper.ProfileMapper;
import com.nta.profileservice.mapper.ShipperProfileMapper;
import com.nta.profileservice.repository.ProfileRepository;
import com.nta.profileservice.repository.ShipperProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipperProfileService {
    ShipperProfileRepository shipperProfileRepository;
    VehicleService vehicleService;
    ProfileMapper profileMapper;
    ProfileRepository profileRepository;
    ProfileService profileService;
    private final ShipperProfileMapper shipperProfileMapper;

    @Transactional
    public ShipperProfileResponse createShipperProfile(final ShipperProfileCreationRequest request) {
        final Vehicle v = vehicleService.findVehicleById(request.getVehicleId());
        // TODO: Call to another service to upload images and receive urls
        final List<String> base64 =
                List.of(request.getAvatarBase64(), request.getIdentityFBase64(), request.getIdentityFBase64());
        final List<String> ulrs = List.of("http:example1.com", "http:example2.com", "http:example3.com");

        // Create profile record
        Profile profile = profileMapper.toUserProfile(request);
        profile.setProfileType(ProfileType.DEFAULT);
        profile.setAvatar(ulrs.getFirst());
        profile = profileRepository.save(profile);

        // Create shipper profile
        ShipperProfile shipperProfile = shipperProfileMapper.toShipperProfile(request);
        shipperProfile.setProfile(profile);
        shipperProfile.setVehicle(v);
        shipperProfile.setIdentityF(ulrs.get(1));
        shipperProfile.setIdentityB(ulrs.get(2));
        shipperProfile = shipperProfileRepository.save(shipperProfile);
        return shipperProfileMapper.toShipperProfileResponse(shipperProfile);
    }

    public ShipperProfileResponse getShipperProfile(final String accountId) {
        final Profile profile = profileService.findByAccountId(accountId);
        final ShipperProfile shipperProfile = shipperProfileRepository
                .findByProfileId(profile.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        final ShipperProfileResponse shipperProfileResponse =
                shipperProfileMapper.toShipperProfileResponse(shipperProfile);
        shipperProfileResponse.setProfile(profileMapper.toUserProfileResponse(profile));
        return shipperProfileResponse;
    }
}
