package com.mter.vicl.advices;

import com.mter.vicl.services.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response> notFoundInDB(NoSuchElementException e){
        log.info(e.getMessage(), e);
        return new ResponseEntity<>(new Response("Not found resource"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoAuthStudentInClassroomException.class)
    public ResponseEntity<Response> authException(NoAuthStudentInClassroomException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoAuthTeacherInClassroomException.class)
    public ResponseEntity<Response> authException(NoAuthTeacherInClassroomException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundTaskException.class)
    public ResponseEntity<Response> notFoundTask(NotFoundTaskException e){
        log.info(e.getMessage(), e);
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationServiceException.class})
    public ResponseEntity<Response> authServiceException(AuthenticationServiceException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({FileStorageException.class})
    public ResponseEntity<Response> fileStorageException(FileStorageException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NotFoundFileException.class})
    public ResponseEntity<Response> notFoundFile(NotFoundFileException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new Response("Wrong format json", ex.getMessage()), status);
    }
}
