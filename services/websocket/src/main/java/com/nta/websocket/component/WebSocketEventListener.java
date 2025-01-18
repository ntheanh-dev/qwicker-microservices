package com.nta.websocket.component;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.nta.websocket.dto.request.internal.ChangeAccountStatusRequest;
import com.nta.websocket.enums.internal.AccountStatus;
import com.nta.websocket.model.AuthenticatedAccountDetail;
import com.nta.websocket.repository.httpClient.IdentityClient;
import com.nta.websocket.service.WebsocketService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketEventListener {
    WebsocketService onlineOfflineService;
    IdentityClient identityClient;

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
        final AuthenticatedAccountDetail userDetails = onlineOfflineService.getUserDetailFromWsSession(event);
        identityClient.changeStatusById(
                userDetails.getId(),
                ChangeAccountStatusRequest.builder()
                        .status(AccountStatus.OFFLINE)
                        .build());
        onlineOfflineService.removeOnlineAccount(userDetails);
    }

    @EventListener
    public void handleConnectedEvent(final SessionConnectedEvent event) {
        final AuthenticatedAccountDetail userDetails = onlineOfflineService.getUserDetailFromWsSession(event);
        identityClient.changeStatusById(
                userDetails.getId(),
                ChangeAccountStatusRequest.builder()
                        .status(AccountStatus.ONLINE)
                        .build());
        onlineOfflineService.addOnlineAccount(userDetails);
    }
    //
    //    @EventListener
    //    @SendToUser
    //    public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
    //        String subscribedChannel =
    //                (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination");
    //        String simpSessionId =
    //                (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpSessionId");
    //        if (subscribedChannel == null) {
    //            log.error("SUBSCRIBED TO NULL?? WAT?!");
    //            return;
    //        }
    //        simpSessionIdToSubscriptionId.put(simpSessionId, subscribedChannel);
    //        onlineOfflineService.addAccountSubscribed(sessionSubscribeEvent.getUser(), subscribedChannel);
    //    }
    //
    //    @EventListener
    //    public void handleUnSubscribeEvent(SessionUnsubscribeEvent unsubscribeEvent) {
    //        String simpSessionId =
    //                (String) unsubscribeEvent.getMessage().getHeaders().get("simpSessionId");
    //        String unSubscribedChannel = simpSessionIdToSubscriptionId.get(simpSessionId);
    //        onlineOfflineService.removeAccountSubscribed(unsubscribeEvent.getUser(), unSubscribedChannel);
    //    }
}
