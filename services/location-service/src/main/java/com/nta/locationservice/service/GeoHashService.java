package com.nta.locationservice.service;

import ch.hsr.geohash.GeoHash;
import com.nta.event.dto.*;
import com.nta.locationservice.model.ShipperDetailCache;
import com.nta.locationservice.repository.httpClient.IdentityClient;
import com.nta.locationservice.repository.httpClient.PostClient;
import java.time.LocalDateTime;
import java.util.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GeoHashService {
  GeoOperations<String, String> geoOperations;
  RedisService redisService;
  GoogMapClientService googMapClientService;
  static Integer GEOHASHING_PRECISION = 5;
  KafkaTemplate<String, Object> kafkaTemplate;
  PostClient postClient;
  IdentityClient identityClient;

//  public void startPost(final FindNearestShipperEvent message) {
//    log.info("Received a post: {}", message.getPostId());
//    final ScheduledExecutorService sesY = Executors.newScheduledThreadPool(10);
//    sesY.scheduleWithFixedDelay(
//        () -> {
//          if (findShipper(message)) {
//            sesY.shutdown();
//          }
//        },
//        0,
//        10,
//        TimeUnit.SECONDS);
//  }
//
//  public boolean findShipper(final FindNearestShipperEvent post) {
//    final String postId = post.getPostId();
//    final double latitude = post.getLatitude();
//    final double longitude = post.getLongitude();
//    log.info("*** Finding shipper for post: {} ***", postId);
//    int REQUEST_FINDING_SHIPPER_TIME_OUT = 1;
//    if (post.getTimestamp()
//        .plusMinutes(REQUEST_FINDING_SHIPPER_TIME_OUT)
//        .isBefore(LocalDateTime.now())) {
//      log.info("*** Find shipper request timed out, postId: {} ***", postId);
//      try {
//        postClient.updatePost(postId, PostStatus.TIMED_OUT.name());
//      } catch (Exception e) {
//        log.error("Error while changing status of post", e);
//      }
//      kafkaTemplate.send(
//          "finding-shipper-request-time-out",
//          NotFoundShipperEvent.builder()
//              .postId(postId)
//              .postMessageType(PostMessageType.NOT_FOUND_SHIPPER)
//              .build());
//      return true;
//    }
//    final String nearestShipperId =
//        findNearestShipper(postId, latitude, longitude, post.getVehicleId());
//    return nearestShipperId != null;
//  }
//
//  public String findNearestShipper(
//      final String postId, final double latitude, final double longitude, final String vehicleId) {
//    final Point myPoint = new Point(longitude, latitude);
//    final Circle within = new Circle(myPoint, new Distance(999, Metrics.KILOMETERS));
//    // Get all shipper points in redis and add them to GeoOperation
//    getAllShipperDetailCacheByGeoHash(latitude, longitude)
//        .values()
//        .forEach(
//            s -> {
//              final ShipperDetailCache shipperDetailCache = (ShipperDetailCache) s;
//              if (shipperDetailCache.getVehicleType().equals(vehicleId)) {
//                try {
//                  final boolean isReadyForTakeOrder =
//                      identityClient.isReadyForTakeOrder(shipperDetailCache.getId()).getResult();
//                  final boolean isInBlackList =
//                      redisService.containsElement("REJECT-" + postId, shipperDetailCache.getId());
//                  if (isReadyForTakeOrder && !isInBlackList) {
//                    geoOperations.add(
//                        RedisKey.SHIPPER_LOCATION,
//                        new Point(
//                            shipperDetailCache.getLongitude(), shipperDetailCache.getLatitude()),
//                        shipperDetailCache.getId());
//                  }
//                } catch (Exception e) {
//                  log.error("Error during adding shipper location", e);
//                }
//              }
//            });
//
//    // Query
//    RedisGeoCommands.GeoRadiusCommandArgs query =
//        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
//            .includeCoordinates()
//            .includeDistance()
//            .sortAscending()
//            .limit(5);
//    // Get shippers within radius
//    GeoResults<RedisGeoCommands.GeoLocation<String>> response =
//        geoOperations.radius(RedisKey.SHIPPER_LOCATION, within, query);
//    if (response == null || response.getContent().isEmpty()) {
//      return null;
//    }
//    // Convert to string
//    final List<String> nearLocations =
//        response.getContent().stream()
//            .map(
//                geoLocation -> {
//                  final Point point = geoLocation.getContent().getPoint();
//                  return point.getX() + "," + point.getY();
//                })
//            .collect(Collectors.toList());
//
//    // Call external API to calculate duration
//    final int nearestShipperIndexByDuration =
//        googMapClientService.getNearestShipperIndex(latitude + "," + longitude, nearLocations);
//    final String shipperId =
//        response.getContent().get(nearestShipperIndexByDuration).getContent().getName();
//    if (tryDeliveryRequestToShipper(postId, shipperId)) {
//      return shipperId; // Thành công, trả về shipper này
//    }
//    return null;
//  }
//
//  private boolean tryDeliveryRequestToShipper(String postId, String shipperId) {
//    log.info("*** Pushing delivery request to shipper: {} ***", shipperId);
//    kafkaTemplate.send(
//        "new-post",
//        DeliveryRequestEvent.builder()
//            .postId(postId)
//            .shipperId(shipperId)
//            .postMessageType(PostMessageType.DELIVERY_REQUEST)
//            .build());
//
//    // Bắt đầu đợi phản hồi từ shipper với timeout
//    boolean shipperResponded = waitForShipperResponse(postId, shipperId, 30);
//    if (!shipperResponded) {
//      redisService.addToList("REJECT-" + postId, shipperId);
//      log.info("*** Shipper {} did not respond in time for postId: {} ***", shipperId, postId);
//      return false;
//    }
//    return true;
//  }
//
//  private boolean waitForShipperResponse(String postId, String shipperId, int timeoutSeconds) {
//    try {
//      for (int i = 0; i < timeoutSeconds; i++) {
//        boolean hasResponded = redisService.containsElement("RESPONSE-" + postId, shipperId);
//        if (hasResponded) {
//          log.info("*** Shipper {} responded for postId: {} ***", shipperId, postId);
//          return true;
//        }
//        Thread.sleep(1000); // Chờ 1 giây
//      }
//    } catch (InterruptedException e) {
//      log.error("Error while waiting for shipper response", e);
//    }
//    return false; // Timeout
//  }
//
//  /**
//   *
//   *
//   * <h3>Get all shipper objects stored in redis within a geo</h3>
//   *
//   * @param latitude
//   * @param longitude
//   * @return a map with key is id and object is ShipperDetailCache model
//   */
//  public Map<String, Object> getAllShipperDetailCacheByGeoHash(
//      final double latitude, final double longitude) {
//    final String geoHash = getGeohashing(latitude, longitude);
//    final Map<String, Object> shippersInGeoHash = redisService.getField(geoHash);
//    if (!shippersInGeoHash.isEmpty()) {
//      return shippersInGeoHash;
//    } else {
//      final GeoHash geoHashObject = GeoHash.fromGeohashString(geoHash);
//      // Lấy các geohash lân cận
//      List<String> geoNeighbors = new ArrayList<>();
//      geoNeighbors.add(geoHash); // Thêm geohash hiện tại vào danh sách các neighbors
//      geoNeighbors.addAll(
//          Arrays.stream(geoHashObject.getAdjacent())
//              .map(GeoHash::toBase32) // Chuyển các geohash lân cận thành chuỗi Base32
//              .toList());
//      log.info("Try to find shipper detail cache in near geohash: {}", geoHash);
//
//      // Lấy dữ liệu từ Redis cho tất cả các geohash trong danh sách neighbors
//      return geoNeighbors.stream()
//          .map(redisService::getField)
//          .flatMap(m -> m.entrySet().stream())
//          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//  }
//
  public String getGeohashing(final double latitude, final double longitude) {
    final GeoHash geoHash =
        GeoHash.withCharacterPrecision(latitude, longitude, GEOHASHING_PRECISION);
    return geoHash.toBase32();
  }

  public void updateLocation(final UpdateLocationEvent updateLocationEvent) {
    final String newGeoHash =
        getGeohashing(updateLocationEvent.getLatitude(), updateLocationEvent.getLongitude());
    // --------Delete Old Location if present ----------------
    if (updateLocationEvent.getPrevLatitude() != 0.0 && updateLocationEvent.getLongitude() != 0.0) {
      final String oldGeoHash =
          getGeohashing(
              updateLocationEvent.getPrevLatitude(), updateLocationEvent.getPrevLongitude());
      redisService.delete(oldGeoHash, updateLocationEvent.getShipperId());
    }
    // --------Update Location in Redis-------------------
    if (redisService.isRedisLive()) {
      // ------Location Table-----------
      redisService.hashSet(
          newGeoHash,
          updateLocationEvent.getShipperId(),
          ShipperDetailCache.builder()
              .id(updateLocationEvent.getShipperId())
              .vehicleType(updateLocationEvent.getVehicleId())
              .ts(LocalDateTime.now())
              .latitude(updateLocationEvent.getLatitude())
              .longitude(updateLocationEvent.getLongitude())
              .build());
      // ------Shipper Table-----------
      redisService.set(updateLocationEvent.getShipperId(), newGeoHash);
    } else {
      log.warn("Redis server is offline, cannot update shipper location");
    }
  }

  public ShipperDetailCache getCurrentShipperLocation(final String shipperId) {
    String currentGeoHash = redisService.getKey(shipperId);
    final Map<String, Object> shippersInGeoHash =
        redisService.getField(currentGeoHash); // string as shipper and object as shipperdetailcache
    for (final Map.Entry<String, Object> entry : shippersInGeoHash.entrySet()) {
      final String key = entry.getKey();
      if (key.equals(shipperId)) {
        return (ShipperDetailCache) entry.getValue();
      }
    }
    return null;
  }
}
