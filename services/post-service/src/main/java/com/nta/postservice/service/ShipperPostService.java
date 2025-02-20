package com.nta.postservice.service;

import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.enums.ErrorCode;
import com.nta.postservice.enums.ShipperPostStatus;
import com.nta.postservice.exception.AppException;
import com.nta.postservice.repository.ShipperPostRepository;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipperPostService {
  ShipperPostRepository shipperPostRepository;
  PostService postService;

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
    return shipperPostRepository.existsByPostIdAndShipper(postId, shipperId);
  }
}
