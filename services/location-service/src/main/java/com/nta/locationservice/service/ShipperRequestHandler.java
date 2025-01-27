package com.nta.locationservice.service;

import ch.hsr.geohash.GeoHash;
import com.nta.event.dto.*;
import com.nta.locationservice.constant.RedisKey;
import com.nta.locationservice.dto.request.internal.PostHistoryCreationRequest;
import com.nta.locationservice.dto.request.internal.ShipperPostCreationRequest;
import com.nta.locationservice.entity.internal.Post;
import com.nta.locationservice.entity.internal.ShipperPost;
import com.nta.locationservice.enums.internal.PostHistoryStatus;
import com.nta.locationservice.enums.internal.PostStatus;
import com.nta.locationservice.model.ShipperDetailCache;
import com.nta.locationservice.repository.httpClient.IdentityClient;
import com.nta.locationservice.repository.httpClient.PostClient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShipperRequestHandler {
  GeoOperations<String, String> geoOperations;
  RedisService redisService;
  GoogMapClientService googMapClientService;
  static Integer GEOHASHING_PRECISION = 5;
  KafkaTemplate<String, Object> kafkaTemplate;
  PostClient postClient;
  IdentityClient identityClient;

  static int REQUEST_FINDING_SHIPPER_TIMEOUT_MINUTES = 5;
  static int SHIPPER_RESPONSE_TIMEOUT_SECONDS = 90;
  static int SCHEDULE_INTERVAL_SECONDS = 10;

  public void startPost(final FindNearestShipperEvent message) {
    log.info("Received a post: {}", message.getPostId());
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleWithFixedDelay(
        () -> {
          if (processShipperFinding(message)) {
            scheduler.shutdown();
          }
        },
        0,
        SCHEDULE_INTERVAL_SECONDS,
        TimeUnit.SECONDS);
  }

  private boolean processShipperFinding(final FindNearestShipperEvent message) {
    final String postId = message.getPostId();
    log.info("*** Trying to find shipper for post: {} ***", postId);

    // Check timeout for finding shipper request
    if (message
        .getTimestamp()
        .plusMinutes(REQUEST_FINDING_SHIPPER_TIMEOUT_MINUTES)
        .isBefore(LocalDateTime.now())) {
      log.info("*** Find shipper request timed out, postId: {} ***", postId);
      handlePostTimeout(postId);
      return true;
    }

    // Find and send request to nearest shipper
    return findAndRequestShipper(message);
  }

  private void handlePostTimeout(final String postId) {
    try {
      postClient.updatePost(postId, Post.builder().status(PostStatus.TIMED_OUT).build());
      postClient.addPostHistory(
          PostHistoryCreationRequest.builder()
              .postId(postId)
              .status(PostHistoryStatus.TIME_OUT)
              .build());
      kafkaTemplate.send(
          "finding-shipper-request-time-out",
          NotFoundShipperEvent.builder()
              .postId(postId)
              .postMessageType(PostMessageType.NOT_FOUND_SHIPPER)
              .build());
    } catch (Exception e) {
      log.error("Error while changing status of post", e);
    }
  }

  private boolean findAndRequestShipper(final FindNearestShipperEvent message) {
    final String postId = message.getPostId();
    final double latitude = message.getLatitude();
    final double longitude = message.getLongitude();

    final String candidateShipper =
        findCandidateShipper(postId, latitude, longitude, message.getVehicleId());
    if (candidateShipper != null) {
      return sendDeliveryRequest(message, candidateShipper);
    }
    return false;
  }

  private String findCandidateShipper(
      final String postId, final double latitude, final double longitude, final String vehicleId) {
    final Point center = new Point(longitude, latitude);
    final Circle searchArea = new Circle(center, new Distance(999, Metrics.KILOMETERS));

    final Map<String, Object> shippers = getAllShipperDetailCacheByGeoHash(latitude, longitude);
    redisService.delete(RedisKey.SHIPPER_LOCATION);
    shippers
        .values()
        .forEach(shipper -> processShipper(postId, (ShipperDetailCache) shipper, vehicleId));

    final RedisGeoCommands.GeoRadiusCommandArgs queryArgs =
        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
            .includeCoordinates()
            .includeDistance()
            .sortAscending()
            .limit(5);

    final GeoResults<RedisGeoCommands.GeoLocation<String>> response =
        geoOperations.radius(RedisKey.SHIPPER_LOCATION, searchArea, queryArgs);
    redisService.delete(RedisKey.SHIPPER_LOCATION);
    if (response == null || response.getContent().isEmpty()) {
      return null;
    }

    if (response.getContent().size() > 1) {
      final List<String> nearLocations =
          response.getContent().stream()
              .map(
                  geoLocation -> {
                    final Point point = geoLocation.getContent().getPoint();
                    return point.getY() + "," + point.getX();
                  })
              .collect(Collectors.toList());

      // Call external API to calculate duration
      final int nearestShipperIndexByDuration =
          googMapClientService.getNearestShipperIndex(latitude + "," + longitude, nearLocations);
      return response.getContent().get(nearestShipperIndexByDuration).getContent().getName();
    } else {
      return response.getContent().getFirst().getContent().getName();
    }
  }

  private boolean sendDeliveryRequest(
      final FindNearestShipperEvent message, final String candidateShipper) {
    log.info("*** Sending delivery request to shipper: {} ***", candidateShipper);
    kafkaTemplate.send(
        "new-post",
        DeliveryRequestEvent.builder()
            .postId(message.getPostId())
            .shipperId(candidateShipper)
            .postMessageType(PostMessageType.DELIVERY_REQUEST)
            .postResponse(message.getPostResponse())
            .build());
    postClient.addPostHistory(
        PostHistoryCreationRequest.builder()
            .postId(message.getPostId())
            .status(PostHistoryStatus.INVITED)
            .description("SHIPPER-" + candidateShipper + "was invited to take this order")
            .build());
    postClient.addShipperPost(
        ShipperPostCreationRequest.builder()
            .postId(message.getPostId())
            .shipperId(candidateShipper)
            .build());
    final boolean isShipperAccept = waitForShipperResponse(message.getPostId(), candidateShipper);
    if (isShipperAccept) {
      return true;
    } else {
      // Reset interval
      message.setTimestamp(LocalDateTime.now());
      return false;
    }
  }

  private boolean waitForShipperResponse(final String postId, final String shipperId) {
    try {
      for (int i = 0; i < SHIPPER_RESPONSE_TIMEOUT_SECONDS; i = i + 5) {
        final ShipperPost lastShipperPost =
            postClient.getLastShipperPostByPostId(postId).getResult();
        switch (lastShipperPost.getStatus()) {
          case INVITED:
            Thread.sleep(5000);
            break;
          case REJECTED:
            return false;
          case ACCEPTED:
            return true;
        }
      }
    } catch (Exception e) {
      log.error("Error while waiting for shipper response", e);
    }

    // Add shipper to rejection list if no response
    redisService.addToList("REJECT-" + postId, shipperId);
    log.info("*** Shipper {} did not respond in time for postId: {} ***", shipperId, postId);
    return false;
  }

  private void processShipper(
      final String postId, final ShipperDetailCache shipperDetailCache, final String vehicleId) {
    if (shipperDetailCache.getVehicleType().equals(vehicleId)) {
      try {
        if (redisService.containsElement("REJECT-" + postId, shipperDetailCache.getId())) return;

        final boolean isReadyForTakeOrder =
            identityClient.isReadyForTakeOrder(shipperDetailCache.getId()).getResult();

        if (isReadyForTakeOrder) {
          geoOperations.add(
              RedisKey.SHIPPER_LOCATION,
              new Point(shipperDetailCache.getLongitude(), shipperDetailCache.getLatitude()),
              shipperDetailCache.getId());
        }
      } catch (Exception e) {
        log.error("Error while processing shipper", e);
      }
    }
  }

  private Map<String, Object> getAllShipperDetailCacheByGeoHash(
      final double latitude, final double longitude) {
    final String geoHash = getGeohashing(latitude, longitude);
    final Map<String, Object> shippers = redisService.getField(geoHash);

    if (!shippers.isEmpty()) {
      return shippers;
    }

    final GeoHash geoHashObject = GeoHash.fromGeohashString(geoHash);
    final List<String> geoNeighbors = new ArrayList<>();
    geoNeighbors.add(geoHash);
    geoNeighbors.addAll(Arrays.stream(geoHashObject.getAdjacent()).map(GeoHash::toBase32).toList());

    return geoNeighbors.stream()
        .map(redisService::getField)
        .flatMap(m -> m.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private String getGeohashing(double latitude, double longitude) {
    GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, GEOHASHING_PRECISION);
    return geoHash.toBase32();
  }
}
