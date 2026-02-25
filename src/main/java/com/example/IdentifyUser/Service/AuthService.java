package com.example.IdentifyUser.Service;

import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.Exception.AppException;
import com.example.IdentifyUser.Exception.ErrorCode;
import com.example.IdentifyUser.Repository.UserRepository;
import com.example.IdentifyUser.dto.reponse.AuthenticationResponse;
import com.example.IdentifyUser.dto.reponse.IntrospectResponse;
import com.example.IdentifyUser.dto.request.AuthenticationRequest;
import com.example.IdentifyUser.dto.request.IntrospectRequest;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest req){
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.UNAUTHENTICATED_ACCESS);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }
    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("huypro37")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", BuildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        // Sign the JWS object
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (Exception e){
            log.error("Cannot sign JWS object: {}", e.getMessage());
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    public IntrospectResponse introspectResponse(IntrospectRequest req)
            throws JOSEException, ParseException {
        String token = req.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var verified = signedJWT.verify(verifier);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return  IntrospectResponse.builder()
                .isActive(verified && expirationTime.after(new Date()))
                .build();
    }

    private String BuildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

//        if (!CollectionUtils.isEmpty(user.getRoles()))
//            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }
}
