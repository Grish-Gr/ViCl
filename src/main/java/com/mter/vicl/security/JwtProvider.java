package com.mter.vicl.security;

import com.mter.vicl.entities.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.refresh-token.expire_time}")
    private Long expireMinutesRefreshToken;
    @Value("${jwt.access-token.expire_time}")
    private Long expireMinutesAccessToken;
    private final SecretKey refreshJwtsecretKey;
    private final SecretKey accessJwtsecretKey;

    public JwtProvider(
        @Value("${jwt.refresh-token.secret_key}") String refreshJwtSecret,
        @Value("${jwt.access-token.secret_key}") String accessJwtSecret
    ){
        this.refreshJwtsecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshJwtSecret));
        this.accessJwtsecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessJwtSecret));
    }

    public String generateAccessToken(Long id, Role role, String name, String lastname){
        return generateToken(id, role, name, lastname, expireMinutesAccessToken, accessJwtsecretKey);
    }

    public String generateRefreshToken(Long id, Role role, String name, String lastname){
        return generateToken(id, role, name, lastname, expireMinutesRefreshToken, refreshJwtsecretKey);
    }

    private String generateToken(
        Long id, Role role,
        String name, String lastname,
        Long expireMinute, SecretKey secretKey
    ){
        Instant expirationDate = Instant.now().plus(expireMinute, ChronoUnit.MINUTES);
        return Jwts.builder()
            .setSubject(id.toString())
            .setExpiration(Date.from(expirationDate))
            .signWith(secretKey)
            .claim("name", name)
            .claim("lastname", lastname)
            .claim("role", role.name())
            .compact();
    }

    public boolean validateRefreshToken(String token){
        return validateToken(token, refreshJwtsecretKey);
    }

    public boolean validateAccessToken(String token){
        return validateToken(token, accessJwtsecretKey);
    }

    public Claims getClaimsRefreshToken(String token){
        return getClaimsByToken(token, refreshJwtsecretKey);
    }

    public Claims getClaimsAccessToken(String token){
        return getClaimsByToken(token, accessJwtsecretKey);
    }

    public boolean validateToken(String token, SecretKey secretKey){
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private Claims getClaimsByToken(String token, SecretKey secretKey){
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
