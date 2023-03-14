package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.services.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/student")
    public ResponseEntity<?> loginStudent(@RequestBody LoginFormDto loginFormDto){
        String token = authenticationService.getJwtTokenStudent(loginFormDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/teacher")
    public ResponseEntity<?> loginTeacher(@RequestBody LoginFormDto loginFormDto){
        String token = authenticationService.getJwtTokenTeacher(loginFormDto);
        return ResponseEntity.ok(token);
    }
}
