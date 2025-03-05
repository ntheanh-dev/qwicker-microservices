package com.nta.postservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nta.postservice.dto.request.RatingCreationRequest;
import com.nta.postservice.dto.response.internal.RatingResponse;
import com.nta.postservice.dto.response.internal.UserProfileResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.Rating;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.enums.ErrorCode;
import com.nta.postservice.enums.ShipperPostStatus;
import com.nta.postservice.exception.AppException;
import com.nta.postservice.repository.RatingRepository;
import com.nta.postservice.repository.httpClient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingService {
    PostService postService;
    RatingRepository ratingRepository;
    AuthenticationService authenticationService;
    ProfileClient profileClient;
    ShipperPostService shipperPostService;

    public Rating create(final RatingCreationRequest request) {
        final ShipperPost shipperPost = shipperPostService.getLastShipperPostByPostId(request.getPostId());
        if (shipperPost == null || !shipperPost.getStatus().equals(ShipperPostStatus.ACCEPTED)) {
            throw new AppException(ErrorCode.POST_NOT_VALID_TO_RATING);
        }
        final Post post = postService.findById(request.getPostId());
        final String raterId = authenticationService.getUserDetail().getId();
        // TODO Check the post status and whether the shipper exists if necessary
        final Rating rating = Rating.builder()
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .shipperId(shipperPost.getShipper())
                .raterId(raterId)
                .postId(post.getId())
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        return ratingRepository.save(rating);
    }

    public List<RatingResponse> getRatingsByShipper(final String shipperId) {
        final List<Rating> ratings = ratingRepository.findByShipperId(shipperId);
        final List<String> raterIds = ratings.stream().map(Rating::getRaterId).toList();
        final Map<String, UserProfileResponse> raterInfoMap =
                profileClient.getUsersByBatch(raterIds).getResult();

        return ratings.stream()
                .map(r -> {
                    final UserProfileResponse raterInfo = raterInfoMap.get(r.getRaterId());
                    return RatingResponse.builder()
                            .shipperId(r.getShipperId())
                            .raterId(r.getRaterId())
                            .postId(r.getPostId())
                            .createdAt(r.getCreatedAt())
                            .feedback(r.getFeedback())
                            .rating(r.getRating())
                            .raterInfo(raterInfo)
                            .build();
                })
                .toList();
    }

    public RatingResponse getRatingByPostId(final String postId) {
        final String raterId = authenticationService.getUserDetail().getId();
        final Rating rating = ratingRepository.findByRaterIdAndPostId(raterId, postId);
        if (rating == null) return null;
        return RatingResponse.builder()
                .rating(rating.getRating())
                .feedback(rating.getFeedback())
                .shipperId(rating.getShipperId())
                .raterId(raterId)
                .postId(raterId)
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
