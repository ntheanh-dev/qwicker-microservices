package com.nta.locationservice.controller;

import com.nta.event.dto.FindNearestShipperEvent;
import com.nta.event.dto.UpdateLocationEvent;
import com.nta.locationservice.service.GeoHashService;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LocationController {
    GeoHashService geoHashService;

    @KafkaListener(topics = "location-update-shipper-location")
    public void listenNotificationSentOTP(final UpdateLocationEvent message) throws IOException {
        geoHashService.updateLocation(message);
    }

    @KafkaListener(topics = "find-nearest-shipper")
    public void listenFindNearestShipper(final FindNearestShipperEvent message) throws IOException {
        geoHashService.startPost(message);
    }
}
