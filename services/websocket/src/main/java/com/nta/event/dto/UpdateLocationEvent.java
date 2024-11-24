package com.nta.event.dto;

import lombok.Data;

@Data
public class UpdateLocationEvent {
    private String shipperId;
    private String vehicleId;
    private double latitude;
    private double longitude;

    private double prevLatitude;
    private double prevLongitude;
    private double timestamp;
}
