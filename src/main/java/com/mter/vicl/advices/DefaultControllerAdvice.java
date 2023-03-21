package com.mter.vicl.advices;

import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import com.mter.vicl.services.exceptions.NotFoundTaskException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response> notFoundInDB(NoSuchElementException e){
        return new ResponseEntity<>(new Response("Not found resource"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoAuthStudentInClassroomException.class)
    public ResponseEntity<Response> authException(NoAuthStudentInClassroomException e){
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoAuthTeacherInClassroomException.class)
    public ResponseEntity<Response> authException(NoAuthTeacherInClassroomException e){
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundTaskException.class)
    public ResponseEntity<Response> notFoundTask(NotFoundTaskException e){
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        return new ResponseEntity<>(new Response("Wrong format json", ex.getMessage()), status);
    }
}
