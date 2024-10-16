package com.nta.identity.mapper;

import org.mapstruct.Mapper;

import com.nta.identity.dto.request.PermissionRequest;
import com.nta.identity.dto.response.PermissionResponse;
import com.nta.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
