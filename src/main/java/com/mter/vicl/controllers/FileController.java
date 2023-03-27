package com.mter.vicl.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage")
public class FileController {

    @GetMapping("/download/supplement-task")
    public ResponseEntity<?> downloadSupplementTask(){

    }
}
