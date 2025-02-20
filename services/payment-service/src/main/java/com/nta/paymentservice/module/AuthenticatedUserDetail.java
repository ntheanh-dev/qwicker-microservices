package com.nta.paymentservice.module;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedUserDetail {
  private String id;
  private String username;
}
