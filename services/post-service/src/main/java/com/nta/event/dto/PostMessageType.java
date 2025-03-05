package com.nta.event.dto;

import lombok.Getter;

@Getter
public enum PostMessageType {
    FINDING_SHIPPER("FINDING_SHIPPER", "Đang tìm shipper phù hợp"),
    SEARCH_TIMEOUT("SEARCH_TIMEOUT", "Hết thời gian tìm shipper, không có ai nhận đơn"),
    SHIPPER_INVITED("SHIPPER_INVITED", "Shipper được mời giao đơn hàng"),
    SHIPPER_FOUND("SHIPPER_FOUND", "Tìm thấy shipper phù hợp"),
    SHIPPER_DECLINED("SHIPPER_DECLINED", "Shipper đã nhận được lời mời nhưng từ chối"),
    SHIPPER_CONFIRMING("SHIPPER_CONFIRMING", "Shipper gọi điện xác nhận đơn hàng đã đặt"),
    SHIPPER_ON_THE_WAY("SHIPPER_ON_THE_WAY", "Shipper trên đường tới nơi lấy hàng"),
    SHIPPER_ARRIVED("SHIPPER_ARRIVED", "Shipper đã tới nơi lấy hàng"),
    PICKED_UP("PICKED_UP", "Shipper đã lấy hàng thành công"),
    DELIVERING("DELIVERING", "Shipper đang giao hàng"),
    DELIVERED("DELIVERED", "Shipper giao hàng thành công"),
    SHIPPER_RECEIVED_PAYMENT("SHIPPER_RECEIVED_PAYMENT", "Shipper nhận tiền thành công"),
    DELIVERY_REQUEST("DELIVERY_REQUEST", "DELIVERY_REQUEST"),
    UPDATE_SHIPPER_LOCATION("UPDATE_SHIPPER_LOCATION", "UPDATE_SHIPPER_LOCATION");
    private final String code;
    private final String description;

    PostMessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }

    public static PostMessageType fromCode(String code) {
        for (PostMessageType status : PostMessageType.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái cho code: " + code);
    }
}
