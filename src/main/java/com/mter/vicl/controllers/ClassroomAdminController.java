package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.ClassroomMessageFormDto;
import com.mter.vicl.dto.request.StatusRecordStudentFormDto;
import com.mter.vicl.dto.response.MessageDto;
import com.mter.vicl.dto.response.RecordStudentDto;
import com.mter.vicl.entities.classroom.ClassroomMessage;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.services.classroom.ClassroomTeacherService;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('CHANGE_CLASSROOM')")
@RequestMapping("/api/v1/classroom")
public class ClassroomAdminController {

    @Autowired
    private ClassroomTeacherService classroomTeacherService;

    @GetMapping("/{classroomID}/unconfirmed-records")
    public ResponseEntity<?> getUnconfirmedStudents(@PathVariable Long classroomID,
                                                    JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        List<RecordStudent> recordStudents = classroomTeacherService.getUnconfirmedStudentsInClassroom(
            authentication.getUserID(), classroomID
        );
        return ResponseEntity.ok(recordStudents.stream().map(RecordStudentDto::from).toList());
    }

    @PutMapping("/{classroomID}/status-record-student")
    public ResponseEntity<?> changeStatusRecordStudent(@RequestBody StatusRecordStudentFormDto statusRecordStudentForm,
                                                       @PathVariable Long classroomID,
                                                       JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        RecordStudent recordStudent = classroomTeacherService.changeStatusRecordStudent(
            authentication.getUserID(),
            classroomID,
            statusRecordStudentForm.studentID(),
            statusRecordStudentForm.getStatusRecord()
        );
        return ResponseEntity.ok(RecordStudentDto.from(recordStudent));
    }

    @PostMapping("/{classroomID}/record/{studentID}")
    public ResponseEntity<?> recordStudentInClassroom(@PathVariable Long classroomID,
                                                      @PathVariable Long studentID,
                                                      JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        RecordStudent recordStudent = classroomTeacherService.addStudentInClassroom(
            authentication.getUserID(), classroomID, studentID
        );
        return ResponseEntity.ok(RecordStudentDto.from(recordStudent));
    }

    @PostMapping("/{classroomID}/message")
    public ResponseEntity<?> sendClassroomMessage(
        @RequestBody ClassroomMessageFormDto messageForm,
        @PathVariable Long classroomID,
        JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        ClassroomMessage classroomMessage = classroomTeacherService.sendMessageInClassroom(
            authentication.getUserID(), classroomID, messageForm
        );
        return ResponseEntity.ok(MessageDto.from(classroomMessage));
    }

    @PostMapping("/{classroomID}/message-with-supplement")
    public ResponseEntity<?> sendClassroomMessage(
        @RequestPart(name = "task") ClassroomMessageFormDto messageForm,
        @PathVariable Long classroomID,
        @RequestPart(name = "file") MultipartFile file,
        JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        ClassroomMessage classroomMessage = classroomTeacherService.sendMessageInClassroom(
            authentication.getUserID(), classroomID, messageForm, file
        );
        return ResponseEntity.ok(MessageDto.from(classroomMessage));
    }
}
