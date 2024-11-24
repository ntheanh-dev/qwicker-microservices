package com.nta.websocket.service;

import java.security.Principal;
import java.util.*;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.nta.websocket.dto.response.Account;
import com.nta.websocket.model.AuthenticatedAccountDetail;
import com.nta.websocket.repository.httpClient.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketService {
    @NonFinal
    Set<String> onlineAccounts;

    @NonFinal
    Map<String, Set<String>> accountSubscribed;

    IdentityClient identityClient;

    public void addOnlineAccount(Principal user) {
        if (user == null) return;
        var userDetails = getUserDetailFromToken(user);
        log.info("{} is online", userDetails.getUsername());
        onlineAccounts.add(userDetails.getId());
    }

    public void removeOnlineAccount(Principal user) {
        if (user != null) {
            var userDetails = getUserDetailFromToken(user);
            log.info("{} went offline", userDetails.getUsername());
            onlineAccounts.remove(userDetails.getId());
            accountSubscribed.remove(userDetails.getId());
        }
    }

    public boolean isAccountOnline(String userId) {
        return onlineAccounts.contains(userId);
    }

    public Map<String, Set<String>> getAccountSubscribed() {
        Map<String, Set<String>> result = new HashMap<>();
        List<Account> users =
                identityClient.findAllAccountsByIds(accountSubscribed.keySet()).getResult();
        users.forEach(user -> result.put(user.getUsername(), accountSubscribed.get(user.getId())));
        return result;
    }

    public void addAccountSubscribed(Principal user, String subscribedChannel) {
        var userDetails = getUserDetailFromToken(user);
        log.info("{} subscribed to {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscriptions = accountSubscribed.getOrDefault(userDetails.getId(), new HashSet<>());
        subscriptions.add(subscribedChannel);
        accountSubscribed.put(userDetails.getId(), subscriptions);
    }

    public void removeAccountSubscribed(Principal user, String subscribedChannel) {
        var userDetails = getUserDetailFromToken(user);
        log.info("unsubscription! {} unsubscribed {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscriptions = accountSubscribed.getOrDefault(userDetails.getId(), new HashSet<>());
        subscriptions.remove(subscribedChannel);
        accountSubscribed.put(userDetails.getId(), subscriptions);
    }

    public boolean isAccountSubscribed(String username, String subscription) {
        Set<String> subscriptions = accountSubscribed.getOrDefault(username, new HashSet<>());
        return subscriptions.contains(subscription);
    }

    public AuthenticatedAccountDetail getUserDetailFromToken(final Principal principal) {
        final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        final Jwt jwt = (Jwt) jwtAuthenticationToken.getCredentials();
        final String subject = jwt.getSubject();
        final String userId = jwt.getClaim("user_id");
        return AuthenticatedAccountDetail.builder().id(userId).username(subject).build();
    }
}
