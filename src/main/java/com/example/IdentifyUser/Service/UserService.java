package com.example.IdentifyUser.Service;


import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.Exception.AppException;
import com.example.IdentifyUser.Exception.ErrorCode;
import com.example.IdentifyUser.MapperRepo.UserMapper;
import com.example.IdentifyUser.Repository.UserRepository;
import com.example.IdentifyUser.dto.reponse.UserResponse;
import com.example.IdentifyUser.dto.request.UserCreationRequest;
import com.example.IdentifyUser.dto.request.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest req){
        if (userRepository.existsByUsername(req.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        User user = userMapper.to_user(req);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        return userMapper.to_user_response(userRepository.save(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest req){
        User user = getUser(id);
        userMapper.updateUser(user, req);
        return userMapper.to_user_response(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers(){
        return userMapper.to_list_users_response(userRepository.findAll());
    }

    public User getUser(String id){
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse getUserResponse(String id){
        User user = getUser(id);
        return userMapper.to_user_response(user);
    }

    public void deleteUser(String id){
        User user = getUser(id);
        userRepository.delete(user);
    }
}
