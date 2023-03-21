package com.mter.vicl.controllers;

import com.mter.vicl.dto.response.AllInfoStudentDto;
import com.mter.vicl.dto.response.InfoStudentDto;
import com.mter.vicl.dto.response.TaskDto;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.services.classroom.ClassroomService;
import com.mter.vicl.services.classroom.ClassroomStudentService;
import com.mter.vicl.services.classroom.ClassroomTeacherService;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@PreAuthorize("hasAuthority('READ_CLASSROOM')")
@RequestMapping("/api/v1/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomStudentService classroomStudentService;
    @Autowired
    private ClassroomTeacherService classroomTeacherService;
    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/{classroomID}/students")
    public ResponseEntity<?> getStudents(@PathVariable Long classroomID, JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        List<Student> students = classroomService.getStudents(
            classroomID, authentication.getUserID(), authentication.getRole()
        );
        return authentication.getRole().equals(Role.STUDENT)
            ? ResponseEntity.ok(students.stream().map(InfoStudentDto::from).toList())
            : ResponseEntity.ok(students.stream().map(AllInfoStudentDto::from).toList());
    }

    @GetMapping("/{classroomID}/tasks")
    public ResponseEntity<?> getTasks(@PathVariable Long classroomID, JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        List<Task> tasks = classroomService.getTasks(
            classroomID, authentication.getUserID(), authentication.getRole()
        );
        return ResponseEntity.ok(tasks.stream().map(TaskDto::from).toList());
    }
}
