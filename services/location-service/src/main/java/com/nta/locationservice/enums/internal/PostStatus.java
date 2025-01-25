package com.nta.locationservice.enums.internal;

public enum PostStatus {
  PENDING,
  WAITING_PAY,
  FOUND_SHIPPER,
  CONFIRM_WITH_CUSTOMER, // shipper confirmed with customer go to pickup location
  SHIPPED, // shipper taken order and go to drop location
  DELIVERED, // shipped
  CANCELLED,
  TIMED_OUT,
  // use for post history only
  COLLECTED_CASH,
  PAID_BY_VNPAY
}
