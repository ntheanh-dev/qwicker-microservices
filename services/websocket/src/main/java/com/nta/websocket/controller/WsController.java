package com.nta.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nta.event.dto.DeliveryRequestEvent;
import com.nta.event.dto.NotFoundShipperEvent;
import com.nta.event.dto.PostMessageType;
import com.nta.event.dto.UpdateLocationEvent;
import com.nta.websocket.dto.response.internal.ShipperProfileResponse;
import com.nta.websocket.model.FoundShipperMessage;
import com.nta.websocket.model.WsMessage;
import com.nta.websocket.model.internal.PostStatus;
import com.nta.websocket.repository.httpClient.PostClient;
import com.nta.websocket.repository.httpClient.ProfileClient;
import com.nta.websocket.service.AuthenticationService;
import java.io.IOException;
import java.security.Principal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WsController {
    SimpMessageSendingOperations simpMessageSendingOperations;
    ObjectMapper objectMapper;
    KafkaTemplate<String, Object> kafkaTemplate;
    AuthenticationService authenticationService;
    ProfileClient profileClient;
    PostClient postClient;

    @MessageMapping("/shipper/{shipperId}")
    public void updateLocation(@Payload WsMessage wsMessage, @DestinationVariable String shipperId, Principal principal)
            throws JsonProcessingException {
        if (wsMessage.getMessageType() != null
                && wsMessage.getMessageType().equals(PostMessageType.UPDATE_SHIPPER_LOCATION)) {
            final UpdateLocationEvent event = objectMapper.readValue(wsMessage.getContent(), UpdateLocationEvent.class);
            log.info("Received update location event: {}", event);
            kafkaTemplate.send("location-update-shipper-location", event);
        }
    }

    @KafkaListener(topics = "new-post")
    public void newPostToTake(final DeliveryRequestEvent message) throws IOException {
        simpMessageSendingOperations.convertAndSend("/topic/shipper/" + message.getShipperId(), message);
    }

    @KafkaListener(topics = "finding-shipper-request-time-out")
    public void newPostToTake(final NotFoundShipperEvent message) throws IOException {
        simpMessageSendingOperations.convertAndSend("/topic/post/" + message.getPostId(), message);
    }

    //    @KafkaListener(topics = "find-shipper")
    //    public void findAppropriateShipperToTakeOrder(final DeliveryRequestEvent message) throws IOException {
    //        simpMessageSendingOperations.convertAndSend("/topic/shipper/" + message.getShipperId(), message);
    //    }

    @KafkaListener(topics = "found-shipper")
    public void foundAppropriateShipperToTakeOrder(final DeliveryRequestEvent message) throws IOException {
        final ShipperProfileResponse shipper = profileClient
                .getShipperProfileByAccountId(message.getShipperId())
                .getResult();
        simpMessageSendingOperations.convertAndSend(
                "/topic/post/" + message.getPostId(),
                FoundShipperMessage.builder()
                        .postResponse(message.getPostResponse())
                        .shipperResponse(objectMapper.writeValueAsString(shipper))
                        .messageType(PostMessageType.FOUND_SHIPPER)
                        .build());
    }

    @MessageMapping("/post/{postId}")
    public void post(@Payload WsMessage wsMessage, @DestinationVariable String postId, Principal principal) {
        if (wsMessage.getMessageType() == null) return;
        switch (wsMessage.getMessageType()) {
            case PostMessageType.REQUEST_JOIN_POST -> {
                final PostStatus currentPostStatus =
                        postClient.findStatusByPostId(postId).getResult();
                final var currentShipper = authenticationService.getUserDetail(principal);
                if (currentPostStatus == PostStatus.PENDING) {
                    final Boolean joinedBefore = postClient
                            .isShipperJoinPost(postId, currentShipper.getId())
                            .getResult();
                    if (joinedBefore) {

                    } else {
                        postClient.shipperJoinPost(postId, currentShipper.getId());
                        // -------Update num joined shipper
                        simpMessageSendingOperations.convertAndSend(
                                "/topic/post/" + postId,
                                WsMessage.builder()
                                        .messageType(PostMessageType.NUM_SHIPPER_JOINED)
                                        .content(String.valueOf(postClient.countShipperJoinedByPostId(postId)))
                                        .build());
                        log.info("Shipper: {}, joined post: {}", currentShipper.getId(), postId);
                    }
                } else {
                    simpMessageSendingOperations.convertAndSend(
                            "/topic/post/" + postId,
                            WsMessage.builder()
                                    .messageType(PostMessageType.POST_WAS_TAKEN)
                                    .build());
                }
            }
            case PostMessageType.SHIPPER_LOCATION -> {
                //                final UpdateShipperLocationRequest s =
                //                        objectMapper.readValue(wsMessage.getContent(),
                // UpdateShipperLocationRequest.class);
                //                simpMessageSendingOperations.convertAndSend(
                //                        "/topic/post/" + postId,
                //                        ShipperLocationResponse.builder()
                //                                .latitude(s.getLatitude())
                //                                .longitude(s.getLongitude())
                //                                .messageType(MessageType.SHIPPER_LOCATION)
                //                                .build());
            }
        }
    }
}
