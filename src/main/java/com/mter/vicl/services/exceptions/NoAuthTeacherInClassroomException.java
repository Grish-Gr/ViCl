package com.mter.vicl.services.exceptions;

import javax.naming.AuthenticationException;

public class NoAuthTeacherInClassroomException extends AuthenticationException {
    public NoAuthTeacherInClassroomException(String message) {
        super(message);
    }
    public NoAuthTeacherInClassroomException(){
        super("Not found Teacher in Classroom");
    }

}
