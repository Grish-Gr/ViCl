package com.mter.vicl.controllers;

import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.services.storage.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/student")
public class StudentAccountController {

    @Autowired
    private FileService fileService;

    @GetMapping("/test")
    public ResponseEntity<?> getTest(Authentication authentication){
        return ResponseEntity.ok(authentication.getPrincipal());
    }

    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.upload(file);
        return ResponseEntity.ok("OK");
    }

    @GetMapping(path = "/download/{fileID}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable Long fileID) throws IOException {
        ResourceDto resourceDto = fileService.downloadFile(fileID);
        ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename(resourceDto.fileInfo().getFileName(), StandardCharsets.UTF_8)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .body(resourceDto.resource());
    }
}
