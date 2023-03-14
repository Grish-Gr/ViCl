package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.services.account.StudentService;
import com.mter.vicl.services.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentAccountController {

    @GetMapping("/test")
    public ResponseEntity<?> getTest(Authentication authentication){
        return ResponseEntity.ok(authentication.getPrincipal());
    }
}
