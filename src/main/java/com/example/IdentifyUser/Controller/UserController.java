package com.example.IdentifyUser.Controller;

import com.example.IdentifyUser.Service.UserService;
import com.example.IdentifyUser.dto.reponse.UserResponse;
import com.example.IdentifyUser.dto.reponse.ApiResponse;
import com.example.IdentifyUser.dto.request.UserCreationRequest;
import com.example.IdentifyUser.dto.request.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest req){
        UserResponse user = userService.createUser(req);
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(user);
        return response;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(){
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setData(userService.getAllUsers());
        return response;
    }

    @GetMapping("{id}")
    ApiResponse<UserResponse> getUser(@PathVariable String id){
        UserResponse user = userService.getUserResponse(id);
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(user);
        return response;
    }

    @PutMapping("{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest req){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.updateUser(id, req));
        return response;
//        return userService.updateUser(id, req);
    }

    @DeleteMapping("{id}")
    ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        ApiResponse<String> response = new ApiResponse<>();
        response.setData("User deleted successfully");
        return response;
    }
}
