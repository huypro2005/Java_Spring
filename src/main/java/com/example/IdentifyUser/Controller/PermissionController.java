package com.example.IdentifyUser.Controller;

import com.example.IdentifyUser.Service.PermissionService;
import com.example.IdentifyUser.dto.reponse.ApiResponse;
import com.example.IdentifyUser.dto.reponse.PermissionResponse;
import com.example.IdentifyUser.dto.request.PermissionRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest req){
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.create(req))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> deletePermission(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<String>builder()
                .data("Permission deleted successfully")
                .build();
    }
}
