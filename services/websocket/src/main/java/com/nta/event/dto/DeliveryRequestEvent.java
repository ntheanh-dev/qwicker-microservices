package com.nta.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequestEvent {
    PostMessageType postMessageType;
    String postResponse;
    String shipperId;
    String postId;
}
