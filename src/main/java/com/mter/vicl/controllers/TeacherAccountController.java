package com.mter.vicl.controllers;


import com.mter.vicl.dto.request.ClassroomFormDto;
import com.mter.vicl.dto.request.LoginFormDto;
import com.mter.vicl.dto.response.ClassroomDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.services.account.TeacherService;
import com.mter.vicl.services.classroom.ClassroomTeacherService;
import com.mter.vicl.services.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('PERSONAL_ACCOUNT_TEACHER')")
@RequestMapping("/teacher")
public class TeacherAccountController {

    @Autowired
    private TeacherService teacherService;

    @PreAuthorize("hasAuthority('CREATE_CLASSROOM')")
    @PostMapping("/classroom")
    public ResponseEntity<?> createClassroom(@RequestBody ClassroomFormDto classroomForm,
                                             JwtAuthentication authentication){
        Classroom classroom = teacherService.createClassroom(
            authentication.getUserID(), classroomForm
        );
        return ResponseEntity.ok(ClassroomDto.from(classroom));
    }

    @GetMapping("/classrooms")
    public ResponseEntity<?> getClassrooms(JwtAuthentication authentication){
        List<Classroom> classrooms = teacherService.getClassrooms(authentication.getUserID());
        return ResponseEntity.ok(classrooms.stream().map(ClassroomDto::from).toList());
    }
}
