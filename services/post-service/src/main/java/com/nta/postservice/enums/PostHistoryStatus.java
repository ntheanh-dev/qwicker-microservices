package com.nta.postservice.enums;

public enum PostHistoryStatus {
    ADDED, // post is just added
    TIME_OUT, // not found any shipper
    INVITED, // one delivery request sent to a shipper
    ACCEPTED, // shipper accept to delivery the order
    REJECTED, // shipper reject to delivery the order
    U_CANCELLED, // user cancel the order
    S_CANCELLED, // shipper cancel the order
}
