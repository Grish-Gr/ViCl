package com.mter.vicl.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Autowired
    private JwtProvider jwtProvider;

    public JwtAuthentication getJwtAuthentication(String token){
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setRole(jwtProvider.getRoleByToken(token));
        jwtAuthentication.setUserID(jwtProvider.getIDByToken(token));
        return jwtAuthentication;
    };
}
