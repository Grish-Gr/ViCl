package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.request.RefreshTokenFormDto;
import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.dto.response.JwtResponseDto;
import com.mter.vicl.dto.response.UserDto;
import com.mter.vicl.entities.users.User;
import com.mter.vicl.services.exceptions.AuthenticationInSystemException;
import com.mter.vicl.services.security.AuthenticationService;
import com.mter.vicl.services.security.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginFormDto loginFormDto){
        log.info("Try login {}", loginFormDto.toString());
        JwtResponseDto response = authenticationService.loginInSystem(loginFormDto);
        log.info("Success login user, put tokens: {}", response.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(@RequestBody RefreshTokenFormDto refreshTokenForm){
        JwtResponseDto response = authenticationService.refreshJwtTokens(refreshTokenForm.refreshToken());
        log.info("Refresh tokens: {}", response.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationFormDto registrationForm){
        User user = registrationService.register(registrationForm);
        log.info("Success registration user in system: {}", user.toString());
        return ResponseEntity.ok(UserDto.from(user));
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(@RequestParam String token) throws AuthenticationInSystemException {
        User user = registrationService.confirmUserInSystem(token);
        log.info("User confirm in system by email: {}", user.toString());
        return ResponseEntity.ok(user);
    }
}
