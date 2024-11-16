package com.nta.postservice.mapper;

import com.nta.postservice.dto.response.PostResponse;
import com.nta.postservice.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
