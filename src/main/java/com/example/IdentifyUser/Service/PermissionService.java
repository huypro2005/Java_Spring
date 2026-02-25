package com.example.IdentifyUser.Service;


import com.example.IdentifyUser.Entity.Permission;
import com.example.IdentifyUser.MapperRepo.PermissionMapper;
import com.example.IdentifyUser.Repository.PermissionRepository;
import com.example.IdentifyUser.dto.reponse.PermissionResponse;
import com.example.IdentifyUser.dto.request.PermissionRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;

    public PermissionResponse create(PermissionRequest req){
        Permission permission = permissionMapper.to_permission(req);
        permission = permissionRepository.save(permission);
        return  permissionMapper.to_permission_response(permission);
    }

    public List<PermissionResponse> getAll(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.to_list_permissions_response(permissions);
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
