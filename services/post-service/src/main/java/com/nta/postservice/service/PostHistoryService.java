package com.nta.postservice.service;

import com.nta.postservice.dto.request.internal.PostHistoryCreationRequest;
import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.PostHistory;
import com.nta.postservice.repository.PostHistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostHistoryService {
  PostHistoryRepository postHistoryRepository;
  PostService postService;

  public void save(final PostHistoryCreationRequest request) {
    final Post post = postService.findById(request.getPostId());
    final PostHistory history =
        PostHistory.builder()
            .post(post)
            .status(request.getStatus())
            .description(request.getDescription())
            .photo(request.getPhoto())
            .build();
    postHistoryRepository.save(history);
  }
}
