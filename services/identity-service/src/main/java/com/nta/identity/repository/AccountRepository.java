package com.nta.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nta.identity.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    Optional<Account> findByUsername(final String username);
}
