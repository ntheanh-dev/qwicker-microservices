package com.nta.profileservice.repository;

import com.nta.profileservice.entity.Profile;
import com.nta.profileservice.enums.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByAccountIdAndProfileType(String accountId, ProfileType profileType);

    Optional<Profile> findByAccountId(String accountId);

    List<Profile> findByAccountIdIn(List<String> accountIds);
}
