package com.example.IdentifyUser.Controller;

import com.example.IdentifyUser.Service.RoleService;
import com.example.IdentifyUser.dto.reponse.ApiResponse;
import com.example.IdentifyUser.dto.reponse.RoleResponse;
import com.example.IdentifyUser.dto.request.RoleRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest req){
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.create(req))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles(){
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<String> delete(@PathVariable String role){
        roleService.delete(role);
        return ApiResponse.<String>builder()
                .data("Permission deleted successfully")
                .build();
    }
}
