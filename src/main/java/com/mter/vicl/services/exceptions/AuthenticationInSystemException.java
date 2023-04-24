package com.mter.vicl.services.exceptions;

import javax.naming.AuthenticationException;

public class AuthenticationInSystemException extends AuthenticationException {
    public AuthenticationInSystemException(String message) {
        super(message);
    }
    public AuthenticationInSystemException() {
        super("Internal exception");
    }
}
