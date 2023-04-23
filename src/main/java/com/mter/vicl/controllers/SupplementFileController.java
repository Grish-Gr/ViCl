package com.mter.vicl.controllers;

import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.security.JwtAuthentication;
import com.mter.vicl.services.storage.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/api/v1/storage")
public class SupplementFileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/download/supplement-task")
    public ResponseEntity<?> downloadSupplementTask(
        @RequestParam(name = "taskID") Long taskID,
        @RequestParam(name = "supplementID") Long supplementID,
        JwtAuthentication authentication
    ) throws IOException {
        ResourceDto resourceDto = fileService.downloadSupplementTask(
            taskID, supplementID, authentication.getUserID(), authentication.getRole()
        );
        ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename(resourceDto.fileInfo().getFileName(), StandardCharsets.UTF_8)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .body(resourceDto.resource());
    }

    @GetMapping("/download/supplement-answer")
    public ResponseEntity<?> downloadSupplementAnswer(
        @RequestParam(name = "answerID") Long answerID,
        @RequestParam(name = "supplementID") Long supplementID,
        JwtAuthentication authentication
    ) throws IOException {
        ResourceDto resourceDto = fileService.downloadSupplementAnswer(
            answerID, supplementID, authentication.getUserID(), authentication.getRole()
        );
        ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename(resourceDto.fileInfo().getFileName(), StandardCharsets.UTF_8)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .body(resourceDto.resource());
    }

    @GetMapping("/download/supplement-message")
    public ResponseEntity<?> downloadSupplementClassroomMessage(
        @RequestParam(name = "classroomMessageID") Long messageID,
        @RequestParam(name = "supplementID") Long supplementID,
        JwtAuthentication authentication
    ) throws IOException {
        ResourceDto resourceDto = fileService.downloadSupplementClassroomMessage(
            messageID, supplementID, authentication.getUserID(), authentication.getRole()
        );
        ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename(resourceDto.fileInfo().getFileName(), StandardCharsets.UTF_8)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .body(resourceDto.resource());
    }
}
