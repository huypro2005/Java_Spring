package com.example.IdentifyUser.Controller;

import com.example.IdentifyUser.Service.AuthService;
import com.example.IdentifyUser.dto.reponse.ApiResponse;
import com.example.IdentifyUser.dto.reponse.AuthenticationResponse;
import com.example.IdentifyUser.dto.reponse.IntrospectResponse;
import com.example.IdentifyUser.dto.request.AuthenticationRequest;
import com.example.IdentifyUser.dto.request.IntrospectRequest;
import com.example.IdentifyUser.dto.request.LogoutRequest;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest req){
//        AuthenticationResponse result = authService.authenticate(req);
//        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
//        response.setData(result);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authService.authenticate(req))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectRequestApiResponse(@RequestBody @NonNull IntrospectRequest req)
            throws ParseException, JOSEException {
//        IntrospectResponse result = authService.introspectResponse(req);
//        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
//        response.setData(result);
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspectResponse(req))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody @NonNull LogoutRequest req)
            throws ParseException, JOSEException {
        authService.Logout(req);
        return ApiResponse.<String>builder()
                .data("Logout successful")
                .build();
    }
}
