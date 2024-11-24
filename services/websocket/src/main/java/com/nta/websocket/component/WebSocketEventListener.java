package com.nta.websocket.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.nta.websocket.service.WebsocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
//    private final WebsocketService onlineOfflineService;
//    private final Map<String, String> simpSessionIdToSubscriptionId = new HashMap<>();
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        onlineOfflineService.removeOnlineAccount(event.getUser());
//    }
//
//    @EventListener
//    public void handleConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
//        onlineOfflineService.addOnlineAccount(sessionConnectedEvent.getUser());
//    }
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
