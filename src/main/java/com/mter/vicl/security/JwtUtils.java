package com.mter.vicl.security;

import com.mter.vicl.entities.users.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {

    @Autowired
    private JwtProvider jwtProvider;

    public JwtAuthentication getJwtAuthentication(String accessToken){
        Claims claims = jwtProvider.getClaimsAccessToken(accessToken);
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setUserID(Long.parseLong(claims.getSubject()));
        jwtAuthentication.setName(claims.get("name", String.class));
        jwtAuthentication.setLastname(claims.get("lastname", String.class));
        jwtAuthentication.setRole(Role.valueOf(claims.get("role", String.class)));
        return jwtAuthentication;
    };

    public String generateNewRefreshToken(String refreshToken){
        Claims claims = jwtProvider.getClaimsRefreshToken(refreshToken);
        return jwtProvider.generateRefreshToken(
            Long.parseLong(claims.getSubject()),
            Role.valueOf(claims.get("role", String.class)),
            claims.get("name", String.class),
            claims.get("lastname", String.class)
        );
    }

    public String generateAccessTokenByRefreshToken(String refreshToken){
        Claims claims = jwtProvider.getClaimsRefreshToken(refreshToken);
        return jwtProvider.generateAccessToken(
            Long.parseLong(claims.getSubject()),
            Role.valueOf(claims.get("role", String.class)),
            claims.get("name", String.class),
            claims.get("lastname", String.class)
        );
    }

    public Long getExpireTimeAccessToken(String accessToken){
        Claims claims = jwtProvider.getClaimsAccessToken(accessToken);
        return claims.getExpiration().getTime() / 1000;
    }
}
