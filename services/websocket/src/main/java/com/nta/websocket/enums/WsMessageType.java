package com.nta.websocket.enums;

public enum WsMessageType {
    REQUEST_DELIVERY,
    SHIPPER_LOCATION,
    UPDATE_POST_STATUS,

    NOT_FOUND_SHIPPER,
    DELIVERY_REQUEST,
    UPDATE_SHIPPER_LOCATION,
    FOUND_SHIPPER,
    APPROVED_SHIPPER,

    NUM_SHIPPER_JOINED,
    REQUEST_JOIN_POST,
    POST_WAS_TAKEN,
}
