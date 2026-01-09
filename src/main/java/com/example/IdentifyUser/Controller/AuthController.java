package com.example.IdentifyUser.Controller;

import com.example.IdentifyUser.Service.AuthService;
import com.example.IdentifyUser.dto.reponse.ApiResponse;
import com.example.IdentifyUser.dto.reponse.AuthenticationResponse;
import com.example.IdentifyUser.dto.request.AuthenticationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> autherticate(@RequestBody AuthenticationRequest req){
        boolean result = authService.authenticate(req);
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setData(new AuthenticationResponse(result));
        return response;
    }
}
