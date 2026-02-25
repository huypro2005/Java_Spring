package com.example.IdentifyUser.Controller;

import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.Service.UserService;
import com.example.IdentifyUser.dto.reponse.UserResponse;
import com.example.IdentifyUser.dto.reponse.ApiResponse;
import com.example.IdentifyUser.dto.request.UserCreationRequest;
import com.example.IdentifyUser.dto.request.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest req){
//        UserResponse user = userService.createUser(req);
//        ApiResponse<UserResponse> response = new ApiResponse<>();
//        response.setData(user);
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(req))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(){

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));


        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("{id}")
    ApiResponse<UserResponse> getUser(@PathVariable String id){
//        UserResponse user = userService.getUserResponse(id);
//        ApiResponse<UserResponse> response = new ApiResponse<>();
//        response.setData(user);
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserResponse(id))
                .build();
    }

    @PutMapping("{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest req){
//        ApiResponse<UserResponse> response = new ApiResponse<>();
//        response.setData(userService.updateUser(id, req));
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(id, req))
                .build();
    }

    @DeleteMapping("{id}")
    ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
//        ApiResponse<String> response = new ApiResponse<>();
//        response.setData("User deleted successfully");
        return ApiResponse.<String>builder()
                .data("User deleted successfully")
                .build();
    }

    @PostMapping("/add-role")
    ApiResponse<String> addRoleToUser(){
        try {
            userService.addRoleToUsers();
            return ApiResponse.<String>builder()
                    .data("Roles added successfully")
                    .build();
        } catch (Exception e) {
            log.error("Error adding roles to users", e);
            return ApiResponse.<String>builder()
                    .data("Failed to add roles to users")
                    .build();
        }
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        try {
            return ApiResponse.<UserResponse>builder()
                    .data(userService.getMyInfo())
                    .build();
        } catch (Exception e) {
            log.error("Error getting user info", e);
            return ApiResponse.<UserResponse>builder()
                    .data(null)
                    .build();
        }
    }
}
