package com.nta.paymentservice.service;

import com.nta.paymentservice.module.AuthenticatedUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  public AuthenticatedUserDetail getUserDetail() {
    final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    final String subject = jwt.getSubject();
    final String userId = jwt.getClaim("userId");
    return AuthenticatedUserDetail.builder().id(userId).username(subject).build();
  }
}
