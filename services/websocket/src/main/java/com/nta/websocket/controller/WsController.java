package com.nta.websocket.controller;

import java.security.Principal;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nta.event.dto.UpdateLocationEvent;
import com.nta.websocket.enums.WsMessageType;
import com.nta.websocket.model.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WsController {
    SimpMessageSendingOperations simpMessageSendingOperations;
    ObjectMapper objectMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    @MessageMapping("/shipper/{shipperId}")
    public void updateLocation(@Payload Message message, @DestinationVariable String shipperId, Principal principal)
            throws JsonProcessingException {
        if (message.getMessageType() != null
                && message.getMessageType().equals(WsMessageType.UPDATE_SHIPPER_LOCATION)) {
            final UpdateLocationEvent event = objectMapper.readValue(message.getContent(), UpdateLocationEvent.class);
            kafkaTemplate.send("location-update-shipper-location", event);
        }
    }
}
