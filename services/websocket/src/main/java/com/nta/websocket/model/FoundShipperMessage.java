package com.nta.websocket.model;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class FoundShipperMessage extends WsMessage {
    String shipperResponse;
    String postResponse;
}
