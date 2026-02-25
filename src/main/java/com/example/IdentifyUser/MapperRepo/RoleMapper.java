package com.example.IdentifyUser.MapperRepo;


import com.example.IdentifyUser.Entity.Permission;
import com.example.IdentifyUser.Entity.Role;
import com.example.IdentifyUser.dto.reponse.PermissionResponse;
import com.example.IdentifyUser.dto.reponse.RoleResponse;
import com.example.IdentifyUser.dto.request.PermissionRequest;
import com.example.IdentifyUser.dto.request.RoleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role to_role(RoleRequest req);
    RoleResponse to_role_response(Role role);
    List<RoleResponse> to_list_roles_response(List<Role> roles);
}
