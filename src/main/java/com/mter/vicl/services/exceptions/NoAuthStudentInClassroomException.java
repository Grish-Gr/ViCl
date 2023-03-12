package com.mter.vicl.services.exceptions;

import javax.naming.AuthenticationException;

public class NoAuthStudentInClassroomException extends AuthenticationException {
    public NoAuthStudentInClassroomException(String message) {
        super(message);
    }
    public NoAuthStudentInClassroomException(){
        super("Not found Student in Classroom");
    }
}
