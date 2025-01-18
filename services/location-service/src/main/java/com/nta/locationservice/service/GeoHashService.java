package com.nta.locationservice.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nta.event.dto.DeliveryRequestEvent;
import com.nta.event.dto.FindNearestShipperEvent;
import com.nta.event.dto.PostMessageType;
import com.nta.event.dto.UpdateLocationEvent;
import com.nta.locationservice.constant.RedisKey;
import com.nta.locationservice.dto.request.internal.ChangeAccountStatusRequest;
import com.nta.locationservice.enums.ErrorCode;
import com.nta.locationservice.enums.internal.AccountStatus;
import com.nta.locationservice.enums.internal.PostStatus;
import com.nta.locationservice.exception.AppException;
import com.nta.locationservice.model.ShipperDetailCache;
import com.nta.locationservice.repository.httpClient.IdentityClient;
import com.nta.locationservice.repository.httpClient.PostClient;

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
    GoogMapClientService googMapClientService;
    static Integer GEOHASHING_PRECISION = 5;
    KafkaTemplate<String, Object> kafkaTemplate;
    PostClient postClient;
    IdentityClient identityClient;

    public void startPost(final FindNearestShipperEvent message) {
        log.info("Received a post: {}", message.getPostId());
        redisService.hashSet("RUNNING_POST", message.getPostId(), message);
    }

    @Scheduled(fixedRate = 10000)
    public void pushDeliveryRequestToShipper() {
        final Map<String, Object> runningPosts = redisService.hashGetAll("RUNNING_POST");
        if (runningPosts.isEmpty()) return;
        runningPosts.forEach((key, value) -> {
            final FindNearestShipperEvent post = (FindNearestShipperEvent) value;
            final String postId = post.getPostId();
            final double latitude = post.getLatitude();
            final double longitude = post.getLongitude();

            final List<String> joinedShipper =
                    postClient.findAllJoinedShipperIdsByPostId(post.getPostId()).getResult();

            if (joinedShipper.isEmpty()) {
                getAllShipperDetailCacheByGeoHash(latitude, longitude).keySet().forEach(shipperId -> {
                    final boolean isShipperOnline =
                            identityClient.isAccountOnline(shipperId).getResult();
                    final boolean isShipperReceivedMessage =
                            redisService.containsElement(RedisKey.SHIPPER_RECEIVED_MSG + ":" + postId, shipperId);
                    if (!isShipperReceivedMessage && !isShipperOnline) {
                        try {
                            log.info(
                                    "============== Pushing delivery request to shipper: {} ==============", shipperId);
                            kafkaTemplate.send(
                                    "new-post",
                                    DeliveryRequestEvent.builder()
                                            .postId(postId)
                                            .shipperId(shipperId)
                                            .postResponse(post.getPostResponse())
                                            .postMessageType(PostMessageType.DELIVERY_REQUEST)
                                            .build());
                            // Lưu lại những shipper da nhan msg
                            redisService.addToList(RedisKey.SHIPPER_RECEIVED_MSG + ":" + postId, shipperId);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                });
            } else {
                final String winShipper = findNearestShipper(joinedShipper, postId, latitude, longitude);

                log.debug("============== Shipper {} was approval to take {} post ==============", winShipper, postId);
                try {
                    // notify figure out win shipper to user, and joined shippers
                    kafkaTemplate.send(
                            "found-shipper",
                            DeliveryRequestEvent.builder()
                                    .postId(postId)
                                    .shipperId(winShipper)
                                    .postResponse(post.getPostResponse())
                                    .postMessageType(PostMessageType.FOUND_SHIPPER)
                                    .build());
                    // update postStatus
                    postClient.changeStatus(postId, PostStatus.FOUND_SHIPPER.name());
                    // update account status
                    identityClient.changeStatusById(
                            winShipper,
                            ChangeAccountStatusRequest.builder()
                                    .status(AccountStatus.SHIPPING)
                                    .build());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

                // remove SHIPPER_RECEIVED_MSG on redis
                redisService.deleteList(RedisKey.SHIPPER_RECEIVED_MSG + postId);
            }
        });
    }

    /**
     * Try to find nearest shipper to take this order
     *
     * @param joinedShipperIds shippers who want to take this order
     * @return shipperId
     */
    public String findNearestShipper(
            final List<String> joinedShipperIds, final String postId, final double latitude, final double longitude) {
        if (joinedShipperIds.size() == 1) { // only one shipper joined
            return joinedShipperIds.getFirst();
        } else { // find nearest shipper
            log.info(
                    "============== Found more than one shipper in this region, trying to find nearest shipper. ==============");
            final Point myPoint = new Point(latitude, latitude);
            final Circle within = new Circle(myPoint, new Distance(999, Metrics.KILOMETERS));
            // Get all shipper points in redis and add them to GeoOperation
            getAllShipperDetailCacheByGeoHash(latitude, longitude).values().forEach(s -> {
                ShipperDetailCache shipperDetailCache = (ShipperDetailCache) s;
                if (joinedShipperIds.contains(((ShipperDetailCache) s).getId())) {
                    geoOperations.add(
                            RedisKey.SHIPPER_LOCATION,
                            new Point(shipperDetailCache.getLongitude(), shipperDetailCache.getLatitude()),
                            postId);
                }
            });

            // Query
            RedisGeoCommands.GeoRadiusCommandArgs query = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                    .includeCoordinates()
                    .includeDistance()
                    .sortAscending()
                    .limit(5);
            // Get shippers within radius
            GeoResults<RedisGeoCommands.GeoLocation<String>> response =
                    geoOperations.radius(RedisKey.SHIPPER_LOCATION, within, query);
            if (response == null || response.getContent().isEmpty()) {
                log.warn("Cannot find any shipper within {} km", 999);
                throw new AppException(ErrorCode.CANNOT_FIND_SHIPPER_IN_REDIS);
            }
            // Convert to string
            final List<String> nearLocations = response.getContent().stream()
                    .map(geoLocation -> {
                        final Point point = geoLocation.getContent().getPoint();
                        return point.getX() + "," + point.getY();
                    })
                    .collect(Collectors.toList());

            // Call external API to calculate duration
            int nearestShipperIndexByDuration =
                    googMapClientService.getNearestShipperIndex(latitude + "," + longitude, nearLocations);
            return response.getContent()
                    .get(nearestShipperIndexByDuration)
                    .getContent()
                    .getName();
        }
    }

    /**
     * <h3>Get all shipper objects stored in redis within a geo</h3>
     *
     * @param latitude
     * @param longitude
     * @return a map with key is id and object is ShipperDetailCache model
     */
    public Map<String, Object> getAllShipperDetailCacheByGeoHash(final double latitude, final double longitude) {
        final String geoHash = getGeohashing(latitude, longitude);
        final Map<String, Object> shippersInGeoHash = redisService.getField(geoHash);
        if (!shippersInGeoHash.isEmpty()) {
            return shippersInGeoHash;
        } else {
            final GeoHash geoHashObject = GeoHash.fromGeohashString(geoHash);
            // Lấy các geohash lân cận
            List<String> geoNeighbors = new ArrayList<>();
            geoNeighbors.add(geoHash); // Thêm geohash hiện tại vào danh sách các neighbors
            geoNeighbors.addAll(Arrays.stream(geoHashObject.getAdjacent())
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
        final GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, GEOHASHING_PRECISION);
        return geoHash.toBase32();
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
