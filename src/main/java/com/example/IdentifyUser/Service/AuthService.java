package com.example.IdentifyUser.Service;

import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.Exception.AppException;
import com.example.IdentifyUser.Exception.ErrorCode;
import com.example.IdentifyUser.Repository.UserRepository;
import com.example.IdentifyUser.dto.reponse.AuthenticationResponse;
import com.example.IdentifyUser.dto.request.AuthenticationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public boolean authenticate(AuthenticationRequest req){
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (passwordEncoder.matches(req.getPassword(), user.getPassword())){
            return true;
        }
        return false;
    }
}
