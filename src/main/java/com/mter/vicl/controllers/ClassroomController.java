package com.mter.vicl.controllers;

import com.mter.vicl.entities.users.Role;
import com.mter.vicl.security.UserPrincipal;
import com.mter.vicl.services.classroom.ClassroomStudentService;
import com.mter.vicl.services.classroom.ClassroomTeacherService;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomStudentService classroomStudentService;
    @Autowired
    private ClassroomTeacherService classroomTeacherService;

    @GetMapping("/{classroomID}/students")
    public ResponseEntity<?> getStudents(@PathVariable Long classroomID, UserPrincipal userPrincipal
    ) throws NoAuthStudentInClassroomException, NoSuchElementException, NoAuthTeacherInClassroomException {
        if (userPrincipal.getRole().equals(Role.STUDENT)){
            return ResponseEntity.ok(classroomStudentService
                .getStudentsInClassroom(userPrincipal.getId(), classroomID)
            );
        } else {
            return ResponseEntity.ok(classroomTeacherService
                .getStudentsInClassroom(userPrincipal.getId(), classroomID));
        }
    }

    @GetMapping("/{classroomID}/tasks")
    public ResponseEntity<?> getTasks(@PathVariable Long classroomID, UserPrincipal userPrincipal
    ) throws NoAuthStudentInClassroomException, NoSuchElementException, NoAuthTeacherInClassroomException {
        if (userPrincipal.getRole().equals(Role.STUDENT)){
            return ResponseEntity.ok(classroomStudentService
                .getTasksInClassroom(userPrincipal.getId(), classroomID)
            );
        } else {
            return ResponseEntity.ok(classroomTeacherService
                .getTasksInClassroom(userPrincipal.getId(), classroomID));
        }
    }

    @GetMapping("/{classroomID}/task/{taskID}answers")
    public ResponseEntity<?> getAnswers(@PathVariable Long classroomID,
                                        @PathVariable Long taskID,
                                        UserPrincipal userPrincipal
    ) throws NoAuthTeacherInClassroomException, NoSuchElementException{
        return ResponseEntity.ok(classroomTeacherService.getAnswersOnTask(userPrincipal.getId(), taskID));
    }

}
