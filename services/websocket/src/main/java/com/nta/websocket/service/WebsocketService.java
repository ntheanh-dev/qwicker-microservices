package com.nta.websocket.service;

import java.security.Principal;
import java.util.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.nta.websocket.dto.response.Account;
import com.nta.websocket.model.AuthenticatedAccountDetail;
import com.nta.websocket.repository.httpClient.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketService {
    Set<String> onlineAccounts = new HashSet<>();
    Map<String, Set<String>> accountSubscribed = new HashMap<>();
    IdentityClient identityClient;

    public void addOnlineAccount(final AuthenticatedAccountDetail userDetails) {
        log.info("{} is online", userDetails.getUsername());
        onlineAccounts.add(userDetails.getUsername());
    }

    public void removeOnlineAccount(final AuthenticatedAccountDetail userDetails) {
        log.info("{} went offline", userDetails.getUsername());
        onlineAccounts.remove(userDetails.getId());
        accountSubscribed.remove(userDetails.getId());
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

    public AuthenticatedAccountDetail getUserDetailFromWsSession(final AbstractSubProtocolEvent event) {
        return getUserDetailFromToken(Objects.requireNonNull(event.getUser()));
    }
}
