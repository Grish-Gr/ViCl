package com.mter.vicl.controllers;

import com.mter.vicl.dto.request.AnswerFormDto;
import com.mter.vicl.dto.request.GradleAnswerFormDto;
import com.mter.vicl.dto.request.TaskFormDto;
import com.mter.vicl.dto.response.AnswerTaskDto;
import com.mter.vicl.dto.response.TaskDto;
import com.mter.vicl.entities.tasks.AnswerTask;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.services.classroom.ClassroomStudentService;
import com.mter.vicl.services.classroom.ClassroomTeacherService;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ClassroomTaskController {

    @Autowired
    private ClassroomTeacherService classroomTeacherService;
    @Autowired
    private ClassroomStudentService classroomStudentService;

    @PreAuthorize("hasAuthority('CREATE_TASK')")
    @PostMapping("/{classroomID}/task")
    public ResponseEntity<?> createTask(@RequestBody TaskFormDto taskForm,
                                        @PathVariable Long classroomID,
                                        JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        Task task = classroomTeacherService.addTaskInClassroom(
            authentication.getUserID(), classroomID, taskForm
        );
        return ResponseEntity.ok(TaskDto.from(task));
    }

    @PreAuthorize("hasAuthority('PASS_ANSWER")
    @PostMapping("/{classroomID}/{taskID}/answer")
    public ResponseEntity<?> addAnswer(@RequestBody AnswerFormDto answerForm,
                                       @PathVariable Long classroomID,
                                       @PathVariable Long taskID,
                                       JwtAuthentication authentication
    ) throws NoAuthStudentInClassroomException {
        AnswerTask answer = classroomStudentService.putAnswerTask(
            authentication.getUserID(), classroomID, taskID, answerForm
        );
        return ResponseEntity.ok(AnswerTaskDto.from(answer));
    }

    @PreAuthorize("hasAuthority('READ_ANSWER_TASK')")
    @GetMapping("/{classroomID}/{taskID}/answers")
    public ResponseEntity<?> getAnswers(@PathVariable Long classroomId,
                                        @PathVariable Long taskID,
                                        JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        List<AnswerTask> answers = classroomTeacherService.getAnswersOnTask(
            authentication.getUserID(), classroomId, taskID
        );
        return ResponseEntity.ok(answers.stream().map(AnswerTaskDto::from).toList());
    }

    @PreAuthorize("hasAuthority('CREATE_TASK')")
    @PutMapping("/{classroomID}/{taskID}/answer")
    public ResponseEntity<?> addGradleAnswer(@RequestBody GradleAnswerFormDto gradleAnswerForm,
                                             @PathVariable Long classroomID,
                                             @PathVariable Long taskID,
                                             JwtAuthentication authentication
    ) throws NoAuthTeacherInClassroomException {
        AnswerTask answerTask = classroomTeacherService.addGradleAnswer(
            authentication.getUserID(), classroomID, taskID, gradleAnswerForm.answerID(), gradleAnswerForm.gradle()
        );
        return ResponseEntity.ok(AnswerTaskDto.from(answerTask));
    }
}
