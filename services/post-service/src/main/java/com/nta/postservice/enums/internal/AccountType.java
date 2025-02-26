package com.nta.postservice.enums.internal;

import lombok.Getter;

@Getter
public enum AccountType {
  USER("USER"),
  SHIPPER("SHIPPER");

  private final String code;

  AccountType(String code) {
    this.code = code;
  }

  public static AccountType fromCode(String code) {
    for (AccountType status : AccountType.values()) {
      if (status.getCode().equalsIgnoreCase(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Không tìm thấy trạng thái cho code: " + code);
  }
}
