package com.example.IdentifyUser.MapperRepo;


import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.dto.reponse.UserResponse;
import com.example.IdentifyUser.dto.request.UserCreationRequest;
import com.example.IdentifyUser.dto.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User to_user(UserCreationRequest req);
//    @Mapping(target = "password",ignore = true)
    UserResponse to_user_response(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest req);
    @Mapping(target = "password", ignore = true)
    List<UserResponse> to_list_users_response(List<User> users);
}
