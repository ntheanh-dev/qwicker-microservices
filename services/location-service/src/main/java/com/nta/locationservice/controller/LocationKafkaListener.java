package com.nta.locationservice.controller;

import com.nta.event.dto.FindNearestShipperEvent;
import com.nta.event.dto.UpdateLocationEvent;
import com.nta.locationservice.service.GeoHashService;
import com.nta.locationservice.service.ShipperRequestHandler;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class LocationKafkaListener {
  GeoHashService geoHashService;
  ShipperRequestHandler shipperRequestHandler;

  @KafkaListener(topics = "location-update-shipper-location")
  public void listenNotificationSentOTP(final UpdateLocationEvent message) throws IOException {
    geoHashService.updateLocation(message);
  }

  @KafkaListener(topics = "find-nearest-shipper")
  public void listenFindNearestShipper(final FindNearestShipperEvent message) {
    CompletableFuture.runAsync(
        () -> {
          try {
            shipperRequestHandler.startPost(message);
          } catch (Exception e) {
            log.error("Error processing message for postId: {}", message.getPostId(), e);
            // Optionally send to DLT or take additional actions.
          }
        });
  }
}
