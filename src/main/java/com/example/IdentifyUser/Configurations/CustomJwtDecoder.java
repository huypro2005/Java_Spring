package com.example.IdentifyUser.Configurations;

import com.example.IdentifyUser.Exception.AppException;
import com.example.IdentifyUser.Service.AuthService;
import com.example.IdentifyUser.dto.request.IntrospectRequest;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Configuration
public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    AuthService authService;
    NimbusJwtDecoder nimbusJwtDecoder = null;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authService.introspectResponse(
                    IntrospectRequest.builder()
                            .token(token)
                            .build()
            );
            if (!response.isActive()) throw new JwtException("Token invalid");
        }catch (JOSEException | ParseException e){
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
