package com.example.IdentifyUser.Service;


import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.Exception.AppException;
import com.example.IdentifyUser.Exception.ErrorCode;
import com.example.IdentifyUser.MapperRepo.UserMapper;
import com.example.IdentifyUser.Repository.RoleRepository;
import com.example.IdentifyUser.Repository.UserRepository;
import com.example.IdentifyUser.dto.reponse.UserResponse;
import com.example.IdentifyUser.dto.request.UserCreationRequest;
import com.example.IdentifyUser.dto.request.UserUpdateRequest;
import com.example.IdentifyUser.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest req){
        if (userRepository.existsByUsername(req.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.to_user(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

//        var roles = roleRepository.findAllById(req.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.to_user_response(userRepository.save(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest req){
        User user = getUser(id);

        userMapper.updateUser(user, req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        var roles = roleRepository.findAllById(req.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.to_user_response(userRepository.save(user));
    }

    @PreAuthorize("hasAuthority('API_GET_ALL_USERS')")
    public List<UserResponse> getAllUsers(){
        var users = userRepository.findAll();
        return users.stream().map(userMapper::to_user_response).toList();
    }


    public User getUser(String id){
        log.info("Method getUser called with id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @PostAuthorize("hasRole('ADMIN') or returnObject.username == authentication.name")
    public UserResponse getUserResponse(String id){
        User user = getUser(id);
        return userMapper.to_user_response(user);
    }

    public void deleteUser(String id){
        User user = getUser(id);
        userRepository.delete(user);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void addRoleToUsers(){
        List<User> users = userRepository.findAll();
        for (User user: users){
            if (user.getRoles() == null){
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.USER.name());
//                user.setRoles(roles);
                userRepository.save(user);
            }
        }
    }

//    @PostMapping("returnObject.username == authentication.name")
    public UserResponse getMyInfo(){
        try{
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            return userMapper.to_user_response(user);
        }
        catch (Exception e){
            log.error("Error getting user info", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
