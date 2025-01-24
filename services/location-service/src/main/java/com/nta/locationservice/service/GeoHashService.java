package com.nta.locationservice.service;

import ch.hsr.geohash.GeoHash;
import com.nta.event.dto.*;
import com.nta.locationservice.constant.RedisKey;
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
public class GeoHashService {
  GeoOperations<String, String> geoOperations;
  RedisService redisService;
  GoogMapClientService googMapClientService;
  static Integer GEOHASHING_PRECISION = 5;
  KafkaTemplate<String, Object> kafkaTemplate;
  PostClient postClient;
  IdentityClient identityClient;

  public void startPost(final FindNearestShipperEvent message) {
    log.info("Received a post: {}", message.getPostId());
    final ScheduledExecutorService sesY = Executors.newSingleThreadScheduledExecutor();
    sesY.scheduleAtFixedRate(
        () -> {
          if (findShipper(message)) {
            sesY.shutdown();
          }
        },
        0,
        10,
        TimeUnit.SECONDS);
  }

  public boolean findShipper(final FindNearestShipperEvent post) {
    final String postId = post.getPostId();
    final double latitude = post.getLatitude();
    final double longitude = post.getLongitude();
    log.info("*** Finding shipper for post: {} ***", postId);
    int REQUEST_FINDING_SHIPPER_TIME_OUT = 5;
    if (post.getTimestamp().plusSeconds(10).isBefore(LocalDateTime.now())) {
      log.info("*** Find shipper request timed out, postId: {} ***", postId);
      kafkaTemplate.send(
          "finding-shipper-request-time-out",
          NotFoundShipperEvent.builder()
              .postId(postId)
              .postMessageType(PostMessageType.NOT_FOUND_SHIPPER)
              .build());
      return true;
    }
    final String nearestShipperId =
        findNearestShipper(postId, latitude, longitude, post.getVehicleId());
    if (nearestShipperId == null) return false;
    log.info("*** Pushing delivery request to shipper: {} ***", nearestShipperId);
    kafkaTemplate.send(
        "new-post",
        DeliveryRequestEvent.builder()
            .postId(postId)
            .shipperId(nearestShipperId)
            .postResponse(post.getPostResponse())
            .postMessageType(PostMessageType.DELIVERY_REQUEST)
            .build());
    return true;
  }

  public String findNearestShipper(
      final String postId, final double latitude, final double longitude, final String vehicleId) {
    final Point myPoint = new Point(latitude, latitude);
    final Circle within = new Circle(myPoint, new Distance(999, Metrics.KILOMETERS));
    // Get all shipper points in redis and add them to GeoOperation
    getAllShipperDetailCacheByGeoHash(latitude, longitude)
        .values()
        .forEach(
            s -> {
              final ShipperDetailCache shipperDetailCache = (ShipperDetailCache) s;
              if (shipperDetailCache.getVehicleType().equals(vehicleId)) {
                geoOperations.add(
                    RedisKey.SHIPPER_LOCATION,
                    new Point(shipperDetailCache.getLongitude(), shipperDetailCache.getLatitude()),
                    postId);
              }
            });

    // Query
    RedisGeoCommands.GeoRadiusCommandArgs query =
        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
            .includeCoordinates()
            .includeDistance()
            .sortAscending()
            .limit(5);
    // Get shippers within radius
    GeoResults<RedisGeoCommands.GeoLocation<String>> response =
        geoOperations.radius(RedisKey.SHIPPER_LOCATION, within, query);
    if (response == null || response.getContent().isEmpty()) {
      log.warn("Cannot find any shipper within {} km", 999);
      return null;
    }
    // Convert to string
    final List<String> nearLocations =
        response.getContent().stream()
            .map(
                geoLocation -> {
                  final Point point = geoLocation.getContent().getPoint();
                  return point.getX() + "," + point.getY();
                })
            .collect(Collectors.toList());

    // Call external API to calculate duration
    int nearestShipperIndexByDuration =
        googMapClientService.getNearestShipperIndex(latitude + "," + longitude, nearLocations);
    return response.getContent().get(nearestShipperIndexByDuration).getContent().getName();
  }

  /**
   *
   *
   * <h3>Get all shipper objects stored in redis within a geo</h3>
   *
   * @param latitude
   * @param longitude
   * @return a map with key is id and object is ShipperDetailCache model
   */
  public Map<String, Object> getAllShipperDetailCacheByGeoHash(
      final double latitude, final double longitude) {
    final String geoHash = getGeohashing(latitude, longitude);
    final Map<String, Object> shippersInGeoHash = redisService.getField(geoHash);
    if (!shippersInGeoHash.isEmpty()) {
      return shippersInGeoHash;
    } else {
      final GeoHash geoHashObject = GeoHash.fromGeohashString(geoHash);
      // Lấy các geohash lân cận
      List<String> geoNeighbors = new ArrayList<>();
      geoNeighbors.add(geoHash); // Thêm geohash hiện tại vào danh sách các neighbors
      geoNeighbors.addAll(
          Arrays.stream(geoHashObject.getAdjacent())
              .map(GeoHash::toBase32) // Chuyển các geohash lân cận thành chuỗi Base32
              .toList());

      // Lấy dữ liệu từ Redis cho tất cả các geohash trong danh sách neighbors
      return geoNeighbors.stream()
          .map(redisService::getField)
          .filter(Objects::nonNull) // Kiểm tra null trước khi xử lý
          .flatMap(m -> m.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
  }

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
