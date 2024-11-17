package com.nta.postservice.mapper;

import org.mapstruct.Mapper;

import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
