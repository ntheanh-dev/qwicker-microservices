package com.nta.websocket.dto.request.internal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinPostRequest {
    String shipperId;
    String postId;
}
