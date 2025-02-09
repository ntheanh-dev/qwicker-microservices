package com.nta.postservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nta.event.dto.FindNearestShipperEvent;
import com.nta.event.dto.PostMessageType;
import com.nta.event.dto.ShipperRequestTakePostEvent;
import com.nta.postservice.dto.request.PostCreationRequest;
import com.nta.postservice.dto.request.internal.Payment;
import com.nta.postservice.dto.request.internal.UploadImageRequest;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.dto.response.internal.DeliveryLocationResponse;
import com.nta.postservice.dto.response.internal.ShipperProfileResponse;
import com.nta.postservice.dto.response.internal.UploadImageResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.PostHistory;
import com.nta.postservice.entity.Product;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.enums.*;
import com.nta.postservice.exception.AppException;
import com.nta.postservice.mapper.PostMapper;
import com.nta.postservice.mapper.ProductMapper;
import com.nta.postservice.repository.*;
import com.nta.postservice.repository.httpClient.FileClient;
import com.nta.postservice.repository.httpClient.LocationClient;
import com.nta.postservice.repository.httpClient.PaymentClient;
import com.nta.postservice.repository.httpClient.ProfileClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    FileClient fileClient;
    LocationClient locationClient;
    PaymentClient paymentClient;
    ProductMapper productMapper;
    ProductCategoryRepository productCategoryRepository;
    PostRepository postRepository;
    ProductRepository productRepository;
    PostHistoryRepository postHistoryRepository;
    VehicleRepository vehicleRepository;
    PostMapper postMapper;
    AuthenticationService authenticationService;
    KafkaTemplate<String, Object> kafkaTemplate;
    ObjectMapper objectMapper;
    ShipperPostRepository shipperPostRepository;
    ProfileClient profileClient;

    @Transactional
    public Post createPost(final PostCreationRequest request) throws JsonProcessingException {
        // --------------Product-----------------
        log.info("Call file-service to upload product image");
        final Product prod = productMapper.toProduct(request.getProduct());
        prod.setCategory(
                productCategoryRepository
                        .findById(request.getProduct().getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND)));
        final Product savedProd = productRepository.save(prod);
        //        uploadImage(request.getProduct().getFile()).thenApply(uploadImageResponse -> {
        //            savedProd.setImage(uploadImageResponse.getUrl());
        //            return productRepository.save(savedProd);
        //        }).exceptionally(ex -> {
        //            // Log lỗi nếu xảy ra sự cố
        //            log.error("Failed to upload image", ex);
        //            throw new AppException(ErrorCode.CANNOT_UPLOAD_IMAGE);
        //        });
        // --------------Location-----------------
        final DeliveryLocationResponse pickupLocation =
                locationClient
                        .createDeliveryLocation(request.getShipment().getPickupLocation())
                        .getResult();
        final DeliveryLocationResponse dropLocation =
                locationClient.createDeliveryLocation(request.getShipment().getDropLocation()).getResult();
        // ---------------Post----------------------
        final Post post =
                postRepository.save(
                        Post.builder()
                                .userId(authenticationService.getUserDetail().getId())
                                .description(request.getOrder().getDescription())
                                .dropLocationId(dropLocation.getId())
                                .pickupLocationId(pickupLocation.getId())
                                .product(savedProd)
                                .deliveryTimeType(request.getShipment().getDeliveryTimeType())
                                .deliveryTimeRequest(request.getShipment().getDeliveryTimeRequest())
                                .vehicleType(
                                        vehicleRepository
                                                .findById(request.getOrder().getVehicleId())
                                                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND)))
                                .postTime(LocalDateTime.now())
                                .status(
                                        request.getPayment().getMethod().equals(PaymentMethod.CASH)
                                                ? PostStatus.PENDING
                                                : PostStatus.WAITING_PAY)
                                .build());
        // ---------------Post History----------------
        postHistoryRepository.save(
                PostHistory.builder()
                        .status(PostHistoryStatus.ADDED)
                        .post(post)
                        .description("Post was created")
                        .build());
        // -----------------Payment-------------------
        final Payment payment =
                paymentClient
                        .createPayment(
                                Payment.builder()
                                        .postId(post.getId())
                                        .isPosterPay(request.getPayment().isPosterPay())
                                        .price(request.getShipment().getCost())
                                        .paymentMethod(request.getPayment().getMethod())
                                        .build())
                        .getResult();
        // -----------------Push notification to shippers----------------------
        final PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setPayment(payment);
        postResponse.setPickupLocation(pickupLocation);
        postResponse.setDropLocation(dropLocation);
        kafkaTemplate.send(
                "find-nearest-shipper",
                FindNearestShipperEvent.builder()
                        .postId(post.getId())
                        .latitude(pickupLocation.getLatitude())
                        .longitude(pickupLocation.getLongitude())
                        .vehicleId(request.getOrder().getVehicleId())
                        .timestamp(LocalDateTime.now())
                        .km(5)
                        .postResponse(objectMapper.writeValueAsString(postResponse))
                        .build());
        return post;
    }

    @Async
    protected CompletableFuture<UploadImageResponse> uploadImage(final String base64) {
        final UploadImageRequest request =
                UploadImageRequest.builder().isMultiple(false).base64(base64).build();
        return CompletableFuture.completedFuture(fileClient.uploadImage(request).getResult());
    }

    public PostResponse findById(final Map<String, String> params, final String id) {
        Post p = null;
        if (params.containsKey("status")) {
            final PostStatus postStatus = convertToEnum(params.get("status"));
            p =
                    postRepository
                            .findPostByIdAndStatus(id, postStatus)
                            .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        } else {
            p = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        }
        final var postResponse = postMapper.toPostResponse(p);
        // ---------------------Location------------------------
        postResponse.setPickupLocation(
                locationClient.getDeliveryLocationById(p.getPickupLocationId()).getResult());
        postResponse.setDropLocation(
                locationClient.getDeliveryLocationById(p.getDropLocationId()).getResult());
        // ---------------------Payment ------------------------
        postResponse.setPayment(paymentClient.findByPostId(p.getId()).getResult());
        return postResponse;
    }

    @Transactional
    public void accept(final String id) {
        final Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        if (post.getStatus().equals(PostStatus.FOUND_SHIPPER)) {
            throw new AppException(ErrorCode.POST_TAKEN_BY_ANOTHER_SHIPPER);
        }
        post.setStatus(PostStatus.FOUND_SHIPPER);
        postRepository.save(post);
        // insert postHistory entity
        final PostHistory postHistory = PostHistory.builder()
                .post(post)
                .statusChangeDate(LocalDateTime.now())
                .status(PostHistoryStatus.ACCEPTED)
                .build();
        postHistoryRepository.save(postHistory);
        // update shipperPost entity
        final String shipperId = authenticationService.getUserDetail().getId();
        final ShipperPost shipperPost = shipperPostRepository.findByPostAndShipper(post, shipperId).orElseThrow(() -> new AppException(ErrorCode.SHIPPER_POST_NOT_FOUND));
        shipperPost.setJoinedAt(LocalDateTime.now());
        shipperPost.setShipper(shipperId);
        shipperPost.setStatus(ShipperPostStatus.ACCEPTED);
        shipperPostRepository.save(shipperPost);

        // send socket message to inform user that found shipper
        String shipperProfileResponseString = "";
        try {
            final ShipperProfileResponse shipperProfileResponse = profileClient.getShipperProfileByAccountId(shipperId).getResult();
            shipperProfileResponseString = objectMapper.writeValueAsString(shipperProfileResponse);
        } catch (FeignException.FeignClientException | JsonProcessingException feignClientException) {
            log.error(feignClientException.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        kafkaTemplate.send(
                "shipment-accept",
                ShipperRequestTakePostEvent.builder()
                        .postId(id)
                        .shipperId(shipperId)
                        .postMessageType(PostMessageType.FOUND_SHIPPER)
                        .shipperProfile(shipperProfileResponseString)
                        .build());
    }

    public Post findById(final String id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    public List<PostResponse> getPostsByStatusList(final String statusList) {
        final var currentUser = authenticationService.getUserDetail();
        if (statusList == null || statusList.isEmpty()) {
            return postMapper.toPostResponseList(postRepository.findPostsByUserId(currentUser.getId()));
        }
        final List<PostStatus> statusEnumList =
                Arrays.stream(statusList.split(",")).map(this::convertToEnum).toList();
        final List<Post> posts = postRepository.findPostsByStatus(currentUser.getId(), statusEnumList);
        final List<String> pickupLocationIds = posts.stream().map(Post::getPickupLocationId).toList();
        final List<String> dropLocationIds = posts.stream().map(Post::getDropLocationId).toList();
        final List<String> postIds = posts.stream().map(Post::getId).toList();
        final List<DeliveryLocationResponse> pickupLocationResponses =
                locationClient.findByIdList(pickupLocationIds).getResult();
        final List<DeliveryLocationResponse> dropLocationResponses =
                locationClient.findByIdList(dropLocationIds).getResult();
        final List<Payment> payments = paymentClient.findByPostIds(postIds).getResult();
        return posts.stream()
                .map(
                        p -> {
                            final PostResponse postResponse = postMapper.toPostResponse(p);
                            postResponse.setPickupLocation(
                                    pickupLocationResponses.stream()
                                            .filter(pD -> pD.getId().equals(p.getPickupLocationId()))
                                            .findFirst()
                                            .get());
                            postResponse.setDropLocation(
                                    dropLocationResponses.stream()
                                            .filter(pD -> pD.getId().equals(p.getDropLocationId()))
                                            .findFirst()
                                            .get());
                            postResponse.setPayment(
                                    payments.stream()
                                            .filter(pM -> pM.getPostId().equals(p.getId()))
                                            .findFirst()
                                            .get());
                            return postResponse;
                        })
                .toList();
    }

    private PostStatus convertToEnum(final String status) {
        try {
            return PostStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_POST_STATUS);
        }
    }

    public void update(final String postId, final Post update) {
        final Post origin = postRepository.findById(postId).get();
        if (update.getStatus() != null) {
            origin.setStatus(update.getStatus());
        }
        postRepository.save(origin);
    }

    public PostStatus findPostStatusByPostId(final String postId) {
        return postRepository.findPostStatusByPostId(postId);
    }
}
