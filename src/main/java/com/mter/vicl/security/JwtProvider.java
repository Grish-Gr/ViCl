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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret-key}") String jwtSecret){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(Long id, Role role){
        Date expirationDate = Date.from(LocalDateTime.now().plusMinutes(15L)
            .atZone(ZoneId.systemDefault()).toInstant()
        );
        return Jwts.builder()
            .setSubject(id.toString())
            .setExpiration(expirationDate)
            .signWith(secretKey)
            .claim("role", role.name())
            .compact();
    }

    public boolean validateToken(String token){
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

    public Long getIDByToken(String token) {
        return Long.parseLong(getClaimsByToken(token).getSubject());
    }

    public Role getRoleByToken(String token){
        String roleName = getClaimsByToken(token).get("role", String.class);
        return Role.valueOf(roleName);
    }

    private Claims getClaimsByToken(String token){
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
