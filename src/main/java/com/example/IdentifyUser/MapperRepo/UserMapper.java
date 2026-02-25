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

    UserResponse to_user_response(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest req);
}
