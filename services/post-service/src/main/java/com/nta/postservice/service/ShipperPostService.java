package com.nta.postservice.service;

import com.nta.postservice.component.DateUtils;
import com.nta.postservice.dto.response.internal.ShipperProfileResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.enums.ErrorCode;
import com.nta.postservice.enums.ShipperPostStatus;
import com.nta.postservice.exception.AppException;
import com.nta.postservice.repository.ShipperPostRepository;
import com.nta.postservice.repository.httpClient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class ShipperPostService {
    ShipperPostRepository shipperPostRepository;
    PostService postService;
    ProfileClient profileClient;
    AuthenticationService authenticationService;
    DateUtils dateUtils;

    public void add(final String postId, final String shipperId) {
        final Post post = postService.findById(postId);
        final ShipperPost shipperPost =
                ShipperPost.builder()
                        .post(post)
                        .shipper(shipperId)
                        .status(ShipperPostStatus.INVITED)
                        .joinedAt(LocalDateTime.now())
                        .build();
        shipperPostRepository.save(shipperPost);
    }

    public ShipperPost getLastShipperPostByPostId(final String postId) {
        return shipperPostRepository
                .getLastShipperPostByPostId(postId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND));
    }

    public boolean isShipperPostExist(final String postId, final String shipperId) {
        return shipperPostRepository.existsByPost_IdAndShipper(postId, shipperId);
    }

    public ShipperProfileResponse getShipperProfileByPostId(
            final String postId, final String shipperPostStatus) {
        final ShipperPostStatus status = ShipperPostStatus.fromCode(shipperPostStatus);
        final String acceptedShipperId =
                shipperPostRepository.findByPostIdAndStatus(postId, status).getFirst().getShipper();
        return profileClient.getShipperProfileByAccountId(acceptedShipperId).getResult();
    }

    public List<String> findPostIdsByDateRange(
            final String type,
            final String timeType,
            final String shipperId,
            final String startDate,
            final String endDate)
            throws ParseException {
        final LocalDateTime from = dateUtils.parseDateTime(startDate);
        final LocalDateTime end = dateUtils.parseDateTime(endDate);
        List<String> results = null;
        switch (timeType) {
            case "HOURLY" ->
                    results =
                            shipperPostRepository.findPostIdsByShipperAndStatusAndDateRangeHourly(
                                    shipperId, ShipperPostStatus.fromCode(type), from, end);
            case "DAILY" ->
                    results =
                            shipperPostRepository.findPostIdsByShipperAndDateRangeAndAcceptedDaily(
                                    shipperId, ShipperPostStatus.fromCode(type), from, end);
            case "MONTHLY" ->
                    results =
                            shipperPostRepository.findPostIdsByShipperAndStatusAndDateRangeMonthly(
                                    shipperId, ShipperPostStatus.fromCode(type), from, end);
            default -> {
                log.error("Unsupported time type: {}", type);
                return List.of();
            }
        }

        return results;
    }
}
