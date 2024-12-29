package com.nta.websocket.service;

import java.security.Principal;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.nta.websocket.model.AuthenticatedAccountDetail;

@Service
public class AuthenticationService {
    public AuthenticatedAccountDetail getUserDetail(final Principal principal) {
        final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        final Jwt jwt = (Jwt) jwtAuthenticationToken.getCredentials();
        final String subject = jwt.getSubject();
        final String userId = jwt.getClaim("userId");
        return AuthenticatedAccountDetail.builder().id(userId).username(subject).build();
    }
}
