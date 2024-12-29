package com.nta.locationservice.service;

import java.time.LocalDateTime;
import java.util.*;

import com.nta.event.dto.DeliveryRequestEvent;
import com.nta.event.dto.FindNearestShipperEvent;
import com.nta.event.dto.PostMessageType;
import com.nta.locationservice.enums.internal.PostStatus;
import com.nta.locationservice.repository.httpClient.PostClient;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nta.event.dto.UpdateLocationEvent;
import com.nta.locationservice.constant.RedisKey;
import com.nta.locationservice.enums.ErrorCode;
import com.nta.locationservice.exception.AppException;
import com.nta.locationservice.model.ShipperDetailCache;

import ch.hsr.geohash.GeoHash;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GeoHashService {
    GeoOperations<String, String> geoOperations;
    RedisService redisService;
    BingMapClientService bingMapClientService;
    static Integer GEOHASHING_PRECISION = 5;
    KafkaTemplate<String, Object> kafkaTemplate;
    PostClient postClient;

    public void startPost(final FindNearestShipperEvent message) {
        log.info("Received a post: {}", message.getPostId());
        redisService.hashSet("RUNNING_POST", message.getPostId(), message);
    }

    @Scheduled(fixedRate = 10000)
    public void pushDeliveryRequestToShipper() {
        final Map<String, Object> runningPosts = redisService.hashGetAll("RUNNING_POST");
        if (runningPosts.isEmpty()) return;
        runningPosts.forEach((key, value) -> {
            // SELECTED SHIPPER
            final FindNearestShipperEvent post = (FindNearestShipperEvent) value;
            final List<String> joinedShipper = postClient.findAllJoinedShipperIdsByPostId(post.getPostId()).getResult();
            if (joinedShipper.isEmpty()) {
                getShippersDetailCacheByGeoHash(
                        post.getLatitude(), post.getLongitude())
                        .keySet()
                        .forEach(
                                shipperId -> {
                                    if (!redisService.containsElement("RECEIVED_MESSAGE:" + key, shipperId)) {
                                        try {
                                            if(redisService.containsElement("ONLINE_SHIPPER", shipperId)) {
                                                log.info("============== Pushing delivery request to shipper: {} ==============", shipperId);
                                                kafkaTemplate.send("new-post", DeliveryRequestEvent.builder()
                                                        .postId(key)
                                                        .shipperId(shipperId)
                                                        .postResponse(post.getPostResponse())
                                                        .postMessageType(PostMessageType.DELIVERY_REQUEST)
                                                        .build());
                                                // Lưu lại những shipper da nhan msg
                                                redisService.addToList("RECEIVED_MESSAGE:" + key, shipperId);
                                            }
                                        } catch (Exception e) {
                                            log.error(e.getMessage());
                                        }
                                    }
                                });
            } else {
                selectShipperToShip(post, joinedShipper);
            }
        });
    }

    public void selectShipperToShip(final FindNearestShipperEvent event, final List<String> joinedShipperIds) {
        String appropriateShipperId;
        if (joinedShipperIds.size() == 1) { // only one shipper joined
            appropriateShipperId = joinedShipperIds.getFirst();
        } else { // find nearest shipper
            log.info("============== Found more than one shipper in this region, trying to find nearest shipper. ==============");
            appropriateShipperId =
                    findNearestShipperId(
                            joinedShipperIds,
                            event.getPostId(),
                            event.getLatitude(),
                            event.getLongitude(),
                            999);
        }

        log.debug("============== Shipper {} was approval to take {} post ==============", appropriateShipperId, event.getPostId());
        try {
            // notify found shipper to user, and joined shippers
            kafkaTemplate.send("found-shipper", DeliveryRequestEvent.builder()
                    .postId(event.getPostId())
                    .shipperId(appropriateShipperId)
                    .postResponse(event.getPostResponse())
                    .postMessageType(PostMessageType.FOUND_SHIPPER)
                    .build());
            // update postStatus
            postClient.changeStatus(event.getPostId(), PostStatus.FOUND_SHIPPER.name());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        // remove SHIPPER_RECEIVED_MSG on redis
        redisService.deleteList("RECEIVED_MESSAGE:" + event.getPostId());
    }

    // Object o day la ShipperDetailCache and key is shipperId
    public Map<String, Object> getShippersDetailCacheByGeoHash(final double latitude, final double longitude) {
        final String geoHash = getGeohashing(latitude, longitude);
        final Map<String, Object> shippersInGeoHash = redisService.getField(geoHash);
        if (!shippersInGeoHash.isEmpty()) {
            return shippersInGeoHash;
        }
        // try to find shipper in larger space
        final Map<String, Object> result = new HashMap<>();
        log.info("============== Cannot find shipper in {}", geoHash + ", try to find in larger space ============== ");
        findNeighbors(geoHash).forEach(neighBorHash -> {
            log.info("============== Getting shippers in neighBorHash geohash: {} ==============", neighBorHash);
            result.putAll(redisService.getField(neighBorHash));
        });
        return result;
    }

    private void initGeoOperationPoint(
            final List<String> shipperIds, final String keyMember, final Double latitude, final Double longitude) {
        getShippersDetailCacheByGeoHash(latitude, longitude).values().forEach(s -> {
            ShipperDetailCache shipperDetailCache = (ShipperDetailCache) s;
            if (shipperIds.contains(((ShipperDetailCache) s).getId())) {
                geoOperations.add(
                        RedisKey.SHIPPER_LOCATION,
                        new Point(shipperDetailCache.getLongitude(), shipperDetailCache.getLatitude()),
                        keyMember);
            }
        });
    }

    public String findNearestShipperId(
            final List<String> shipperIds,
            final String keyMember,
            final Double latitude,
            final Double longitude,
            final int km) {
        Point myPoint = new Point(longitude, latitude);
        Circle within = new Circle(myPoint, new Distance(km, Metrics.KILOMETERS));
        // Find shipper's location in redis
        initGeoOperationPoint(shipperIds, keyMember, latitude, longitude);
        //

        // Query
        RedisGeoCommands.GeoRadiusCommandArgs query = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .sortAscending()
                .limit(4);
        // Get shippers within radius
        GeoResults<RedisGeoCommands.GeoLocation<String>> response =
                geoOperations.radius(RedisKey.SHIPPER_LOCATION, within, query);
        if (response == null || response.getContent().isEmpty()) {
            log.warn("Cannot find any shipper within {} km", km);
            throw new AppException(ErrorCode.CANNOT_FIND_SHIPPER_IN_REDIS);
        }
        List<Point> nearLocations = response.getContent().stream()
                .map(m -> m.getContent().getPoint())
                .toList();

        // Call external API to calculate duration
        int nearestShipperIndexByDuration =
                bingMapClientService.getNearestShipperIndex(new Point(longitude, latitude), nearLocations);
        return response.getContent()
                .get(nearestShipperIndexByDuration)
                .getContent()
                .getName();
    }

    public String getGeohashing(final double latitude, final double longitude) {
        final GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, GEOHASHING_PRECISION);
        return geoHash.toBase32();
    }

    public List<String> findNeighbors(final String geohashStr) {
        // Tạo một đối tượng GeoHash từ chuỗi geohash
        final GeoHash geoHash = GeoHash.fromGeohashString(geohashStr);
        final List<String> result = new ArrayList<>();
        // Lấy các geohash lân cận
        Arrays.stream(geoHash.getAdjacent()).toList().forEach(g -> result.add(g.toBase32()));
        return result;
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

    public void updateLocation(final UpdateLocationEvent updateLocationEvent) {
        final String newGeoHash = getGeohashing(updateLocationEvent.getLatitude(), updateLocationEvent.getLongitude());
        // --------Delete Old Location if present ----------------
        if (updateLocationEvent.getPrevLatitude() != 0.0 && updateLocationEvent.getLongitude() != 0.0) {
            final String oldGeoHash =
                    getGeohashing(updateLocationEvent.getPrevLatitude(), updateLocationEvent.getPrevLongitude());
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
}
