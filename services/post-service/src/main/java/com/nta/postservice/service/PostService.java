package com.nta.postservice.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.nta.postservice.repository.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nta.postservice.dto.request.Payment;
import com.nta.postservice.dto.request.PostCreationRequest;
import com.nta.postservice.dto.request.UploadImageRequest;
import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.dto.response.UploadImageResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.PostHistory;
import com.nta.postservice.entity.Product;
import com.nta.postservice.enums.ErrorCode;
import com.nta.postservice.enums.PaymentMethod;
import com.nta.postservice.enums.PostStatus;
import com.nta.postservice.exception.AppException;
import com.nta.postservice.mapper.PostMapper;
import com.nta.postservice.mapper.ProductMapper;
import com.nta.postservice.repository.httpClient.FileClient;
import com.nta.postservice.repository.httpClient.LocationClient;
import com.nta.postservice.repository.httpClient.PaymentClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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

    @Transactional
    public Post createPost(final PostCreationRequest request) {
        // --------------Product-----------------
        log.info("Call file-service to upload product image");
        final Product prod = productMapper.toProduct(request.getProduct());
        prod.setCategory(productCategoryRepository
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
        final String pickupLocationId = locationClient
                .createDeliveryLocation(request.getShipment().getPickupLocation())
                .getResult()
                .getId();
        final String dropLocationId = locationClient
                .createDeliveryLocation(request.getShipment().getDropLocation())
                .getResult()
                .getId();
        // TODO Call payment server to persist payment data

        // ---------------Post----------------------
        final Post post = postRepository.save(Post.builder()
                .description(request.getOrder().getDescription())
                .dropLocationId(dropLocationId)
                .pickupLocationId(pickupLocationId)
                .product(savedProd)
                .deliveryTimeType(request.getShipment().getDeliveryTimeType())
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
        postHistoryRepository.save(PostHistory.builder()
                .status(PostStatus.PENDING)
                .post(post)
                .description("Post was created")
                .build());
        // -----------------Payment-------------------
        paymentClient.createPayment(Payment.builder()
                .postId(post.getId())
                .isPosterPay(request.getPayment().isPosterPay())
                .price(request.getShipment().getCost())
                .paymentMethod(request.getPayment().getMethod())
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
            p = postRepository
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

    //    public PostResponse getPostByStatus(final String status, final String postId) {
    //        final PostStatus postStatus = convertToEnum(status);
    //        final var response = postRepository
    //                .findPostByIdAndStatus(postId, postStatus)
    //                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    //        return postMapper.toPostResponse(response);
    //    }

    private PostStatus convertToEnum(final String status) {
        try {
            return PostStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_POST_STATUS);
        }
    }
}
