package com.nta.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindNearestShipperEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 7156526077883281625L;
    String postId;
    Double latitude;
    Double longitude;
    String vehicleId;
    int km;
    String postResponse;
}
