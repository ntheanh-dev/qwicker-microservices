package com.nta.websocket.service;

import java.security.Principal;
import java.util.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    Set<String> onlineAccounts = new HashSet<>();
    Map<String, Set<String>> accountSubscribed = new HashMap<>();
    String ONLINE_SHIPPER_KEY = "ONLINE_SHIPPER";
    IdentityClient identityClient;
    RedisService redisService;
    public void addOnlineAccount(Principal user) {
        if (user == null) return;
        final var userDetails = getUserDetailFromToken(user);
        log.info("{} is online", userDetails.getUsername());
        onlineAccounts.add(userDetails.getUsername());
        redisService.addToList(ONLINE_SHIPPER_KEY, userDetails.getId());
    }

    public void removeOnlineAccount(Principal user) {
        if(user == null) return;
        var userDetails = getUserDetailFromToken(user);
        log.info("{} went offline", userDetails.getUsername());
        onlineAccounts.remove(userDetails.getId());
        accountSubscribed.remove(userDetails.getId());
        redisService.removeFromList(ONLINE_SHIPPER_KEY, userDetails.getId());
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
        final String userId = (String) ((UsernamePasswordAuthenticationToken) principal).getDetails();
        return AuthenticatedAccountDetail.builder().id(userId).username(principal.getName()).build();
    }
}
