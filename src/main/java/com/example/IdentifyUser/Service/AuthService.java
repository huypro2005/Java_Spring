package com.example.IdentifyUser.Service;

import com.example.IdentifyUser.Entity.InvalidateToken;
import com.example.IdentifyUser.Entity.User;
import com.example.IdentifyUser.Exception.AppException;
import com.example.IdentifyUser.Exception.ErrorCode;
import com.example.IdentifyUser.Repository.InvalidateTokenRepository;
import com.example.IdentifyUser.Repository.UserRepository;
import com.example.IdentifyUser.dto.reponse.AuthenticationResponse;
import com.example.IdentifyUser.dto.reponse.IntrospectResponse;
import com.example.IdentifyUser.dto.request.AuthenticationRequest;
import com.example.IdentifyUser.dto.request.IntrospectRequest;
import com.example.IdentifyUser.dto.request.LogoutRequest;
import com.example.IdentifyUser.dto.request.RefreshRequest;
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
import org.springframework.security.crypto.password.AbstractPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    InvalidateTokenRepository invalidateTokenRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.jwt-duration}")
    protected int JWT_DURATION;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected int REFRESH_DURATION;

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
        /*
        * Generate a JWT token with the following claims:
        * - sub: the user's username
        * - iss: the issuer of the token (e.g., your application name)
        * - iat: the time the token was issued
        * - exp: the expiration time of the token (e.g., 1 hour from the time of issuance)
        * - jti: a unique identifier for the token (e.g., a UUID)
        * - scope: a space-separated string of the user's roles and permissions
        * */

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("huypro37")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(JWT_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
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
        /*
        * Check the token's validity by verifying its signature and expiration time.
        * */
        String token = req.getToken();
        boolean invalid = true;

        try {
            verifyToken(token, false);

        } catch (AppException e){
            invalid = false;
        }
        return IntrospectResponse.builder()
                .isActive(invalid)
                .build();
    }

    public void Logout(LogoutRequest req) throws ParseException, JOSEException {
        // Invalidate the token by adding it to a blacklist or removing it from the database
        // This is a simple implementation and may not be suitable for production use
        // You can use a cache or database to store blacklisted tokens and check against it in the verifyToken method
        try {
            var signToken = verifyToken(req.getToken(), true);

            String jti = signToken.getJWTClaimsSet().getJWTID();
            Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .jti(jti)
                    .expirationTime(expirationTime)
                    .build();

            invalidateTokenRepository.save(invalidateToken);
        } catch (AppException e){
            log.info("Token already invalid");
        }

    }

    public AuthenticationResponse refreshToken(RefreshRequest req) throws ParseException, JOSEException {
        // Implement token refresh logic here
        // This typically involves verifying the existing token, checking its validity, and issuing a new token if valid
        var signToken = verifyToken(req.getToken(), true);

        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .jti(jti)
                .expirationTime(expirationTime)
                .build();

        invalidateTokenRepository.save(invalidateToken);

        String username = signToken.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean refresh)
            throws JOSEException, ParseException{
        /*
        * Check the token's signature and expiration time.
        * If the token is invalid or expired, throw an exception.
        * */
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = refresh
                ? new Date (signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(signedJWT.verify(verifier) && expirationTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED_ACCESS);
        }

        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        if (invalidateTokenRepository.existsById(jti)){
            throw new AppException(ErrorCode.UNAUTHENTICATED_ACCESS);
        }

        return  signedJWT;
    }

    private String BuildScope(User user) {
        /*
        * Build the scope string based on the user's roles and permissions.
        * */
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }
}
