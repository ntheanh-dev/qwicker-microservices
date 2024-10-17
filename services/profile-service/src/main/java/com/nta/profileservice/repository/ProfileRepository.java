package com.nta.profileservice.repository;

import java.util.Optional;

import com.nta.profileservice.enums.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nta.profileservice.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByAccountIdAndProfileType(String accountId, ProfileType profileType);
}
