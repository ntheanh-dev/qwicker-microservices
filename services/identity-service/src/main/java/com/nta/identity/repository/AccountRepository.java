package com.nta.identity.repository;

import java.util.Optional;

import com.nta.identity.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUsername(final String username);

    Optional<Account> findByUsername(final String username);
}
