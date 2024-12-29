package com.nta.websocket.component;

import com.nta.websocket.service.RedisService;
import com.nta.websocket.service.WebsocketService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketEventListener {
        WebsocketService onlineOfflineService;
        RedisService redisService;
        Map<String, String> simpSessionIdToSubscriptionId = new HashMap<>();
        @EventListener
        public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
            onlineOfflineService.removeOnlineAccount(event.getUser());
        }

        @EventListener
        public void handleConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
            onlineOfflineService.addOnlineAccount(sessionConnectedEvent.getUser());
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
