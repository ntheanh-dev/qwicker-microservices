package com.nta.paymentservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyPaymentStatistics {
    String month;
    String paymentMethod;
    String totalAmount;
}
