package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.request.RefreshTokenFormDto;
import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.dto.response.JwtResponseDto;
import com.mter.vicl.dto.response.UserDto;
import com.mter.vicl.entities.users.User;
import com.mter.vicl.services.security.AuthenticationService;
import com.mter.vicl.services.security.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("Try login " + loginFormDto.toString());
        log.info("Try login " + loginFormDto.email());
        JwtResponseDto response = authenticationService.loginInSystem(loginFormDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(@RequestBody RefreshTokenFormDto refreshTokenForm){
        JwtResponseDto response = authenticationService.refreshJwtTokens(refreshTokenForm.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationFormDto registrationForm){
        User user = registrationService.register(registrationForm);
        return ResponseEntity.ok(UserDto.from(user));
    }
}
