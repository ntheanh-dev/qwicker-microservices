package com.nta.postservice.service;

import com.nta.postservice.enums.internal.AccountType;
import com.nta.postservice.model.AuthenticatedUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  public AuthenticatedUserDetail getUserDetail() {
    final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    final String subject = jwt.getSubject();
    final String userId = jwt.getClaim("userId");
    final String scope = jwt.getClaim("scope");
    final AuthenticatedUserDetail userDetail =
        AuthenticatedUserDetail.builder().id(userId).username(subject).build();
    if (scope != null && scope.contains(AccountType.USER.toString())) {
      userDetail.setAccountType(AccountType.USER);
    } else if (scope != null && scope.contains(AccountType.SHIPPER.toString())) {
      userDetail.setAccountType(AccountType.SHIPPER);
    }
    return userDetail;
  }
}
