package com.nta.postservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);
}
