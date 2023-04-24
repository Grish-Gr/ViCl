package com.mter.vicl.controllers;

import com.mter.vicl.dto.response.ClassroomDto;
import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.services.account.StudentService;
import com.mter.vicl.services.storage.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@PreAuthorize("hasAuthority('PERSONAL_ACCOUNT_STUDENT')")
@RestController
@RequestMapping("/api/v1/student")
public class StudentAccountController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/classrooms")
    private ResponseEntity<?> getClassrooms(JwtAuthentication authentication){
        return ResponseEntity.ok(studentService.getClassrooms(authentication.getUserID())
            .stream().map(ClassroomDto::from).toList());
    }
}
