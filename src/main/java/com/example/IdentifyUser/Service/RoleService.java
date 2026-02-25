package com.example.IdentifyUser.Service;

import com.example.IdentifyUser.Entity.Role;
import com.example.IdentifyUser.MapperRepo.RoleMapper;
import com.example.IdentifyUser.Repository.PermissionRepository;
import com.example.IdentifyUser.Repository.RoleRepository;
import com.example.IdentifyUser.dto.reponse.RoleResponse;
import com.example.IdentifyUser.dto.request.RoleRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest req){
        var role = roleMapper.to_role(req);
        var permissions = permissionRepository.findAllById(req.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.to_role_response(role);
    }

    public RoleResponse get(String roleName){
        var role = roleRepository.findById(roleName).orElseThrow();
        return roleMapper.to_role_response(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roleMapper.to_list_roles_response(roles);
    }

    public RoleResponse addPermissionToRole(String roleName, String permissionName){
        var role = roleRepository.findById(roleName);
        var permission = permissionRepository.findById(permissionName);
        if (role.isEmpty() || permission.isEmpty()) {
            throw new RuntimeException("Role or permission not found");
        }
        role.get().getPermissions().add(permission.get());
        Role roleUpdate = roleRepository.save(role.get());
        return roleMapper.to_role_response(roleUpdate);
    }

    public RoleResponse deletePermissionFromRole(String roleName, String permissionName){
        var role = roleRepository.findById(roleName);
        var permission = permissionRepository.findById(permissionName);
        if (role.isEmpty() || permission.isEmpty()) {
            throw new RuntimeException("Role or permission not found");
        }
        try {
            role.get().getPermissions().remove(permission.get());
            Role roleUpdate = roleRepository.save(role.get());
            return roleMapper.to_role_response(roleUpdate);
        }catch (Exception e){
            throw new RuntimeException("Permission not found in role");
        }
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
