package com.nta.locationservice.service;

import ch.hsr.geohash.GeoHash;
import com.nta.event.dto.*;
import com.nta.locationservice.model.ShipperDetailCache;
import java.time.LocalDateTime;
import java.util.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GeoHashService {
  RedisService redisService;
  static Integer GEOHASHING_PRECISION = 5;

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
