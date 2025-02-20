package com.nta.postservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShipperPostStatus {
    INVITED("INVITED"),
    REJECTED("REJECTED"),
    ACCEPTED("ACCEPTED");

    private final String code;

    public static ShipperPostStatus fromCode(String code) {
        for (ShipperPostStatus status : ShipperPostStatus.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái cho code: " + code);
    }
}
