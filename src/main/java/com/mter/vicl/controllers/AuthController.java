package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.request.RefreshTokenFormDto;
import com.mter.vicl.dto.request.RegistrationFormDto;
import com.mter.vicl.dto.response.JwtResponseDto;
import com.mter.vicl.services.security.AuthenticationService;
import com.mter.vicl.services.security.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginFormDto loginFormDto){
        JwtResponseDto response = authenticationService.getJwtTokens(loginFormDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(@RequestBody RefreshTokenFormDto refreshTokenForm){
        JwtResponseDto response = authenticationService.refreshJwtTokens(refreshTokenForm.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationFormDto registrationForm){
        return ResponseEntity.ok(registrationService.register(registrationForm));
    }
}
