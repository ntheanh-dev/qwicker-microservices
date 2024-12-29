package com.nta.websocket.model;

import com.nta.event.dto.PostMessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class WsMessage {
    private PostMessageType messageType;
    private String content;
}
