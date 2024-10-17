package com.nta.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nta.identity.dto.request.RoleRequest;
import com.nta.identity.dto.response.RoleResponse;
import com.nta.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
