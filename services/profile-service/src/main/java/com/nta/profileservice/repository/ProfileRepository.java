package com.nta.profileservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.enums.ProfileType;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByAccountIdAndProfileType(String accountId, ProfileType profileType);

    Optional<Profile> findByAccountId(String accountId);
}
