package com.nta.profileservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.nta.profileservice.model.AuthenticatedUserDetail;

@Service
public class AuthenticationService {
    public AuthenticatedUserDetail getAuthenticatedUserDetailFromToken() {
        final Jwt jwt =
                (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        final String subject = jwt.getSubject();
        final String userId = jwt.getClaim("userId");
        return AuthenticatedUserDetail.builder().id(userId).username(subject).build();
    }
}
