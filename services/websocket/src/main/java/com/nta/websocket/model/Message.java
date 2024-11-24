package com.nta.websocket.model;

import com.nta.websocket.enums.WsMessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Message {
    private WsMessageType messageType;
    private String content;
}
