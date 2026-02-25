package com.example.IdentifyUser.MapperRepo;


import com.example.IdentifyUser.Entity.Permission;
import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.dto.reponse.PermissionResponse;
import com.example.IdentifyUser.dto.reponse.UserResponse;
import com.example.IdentifyUser.dto.request.PermissionRequest;
import com.example.IdentifyUser.dto.request.UserCreationRequest;
import com.example.IdentifyUser.dto.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission to_permission(PermissionRequest req);
    PermissionResponse to_permission_response(Permission permission);
    List<PermissionResponse> to_list_permissions_response(List<Permission> permissions);
}
