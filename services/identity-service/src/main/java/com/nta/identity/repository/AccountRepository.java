package com.nta.identity.repository;

import com.nta.identity.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
  boolean existsByUsername(final String username);

  boolean existsByEmail(final String email);

  Optional<Account> findByUsername(final String username);

  @Query(
      "SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END "
          + "FROM Account a WHERE a.id = :accountId AND a.status = 'READY_FOR_TAKE_ORDER'")
  boolean isReadyForTakeOrder(@Param("accountId") String accountId);
}
