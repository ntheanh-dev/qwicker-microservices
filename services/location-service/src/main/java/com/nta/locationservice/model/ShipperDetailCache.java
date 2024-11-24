package com.nta.locationservice.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class ShipperDetailCache implements Serializable {
    @Serial
    private static final long sserialVersionUID = 7156526077883281625L;

    private String id;
    private String vehicleType;
    private LocalDateTime ts;
    private double latitude;
    private double longitude;
}
