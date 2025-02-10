package com.nta.postservice.service;

import com.nta.postservice.dto.request.RatingCreationRequest;
import com.nta.postservice.dto.response.internal.RatingResponse;
import com.nta.postservice.dto.response.internal.UserProfileResponse;
import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.Rating;
import com.nta.postservice.repository.RatingRepository;
import com.nta.postservice.repository.httpClient.ProfileClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingService {
    PostService postService;
    RatingRepository ratingRepository;
    AuthenticationService authenticationService;
    ProfileClient profileClient;

    public Rating create(final RatingCreationRequest request) {
        final Post post = postService.findById(request.getPostId());
        final String raterId = authenticationService.getUserDetail().getId();
        //TODO Check the post status and whether the shipper exists if necessary
        final Rating rating = Rating.builder()
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .shipperId(request.getShipperId())
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
        final Map<String, UserProfileResponse> raterInfoMap = profileClient.getUsersByBatch(raterIds).getResult();

        return ratings.stream().map(r -> {
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
        }).toList();
    }
}
