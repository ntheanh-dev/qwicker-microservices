package com.nta.paymentservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatisticIncomeType {
    HOURLY("HOURLY"),
    DAILY("DAILY"),
    MONTHLY("MONTHLY");

    private final String code;

    public static StatisticIncomeType fromCode(String code) {
        for (StatisticIncomeType status : StatisticIncomeType.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái cho code: " + code);
    }
}
