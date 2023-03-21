package com.mter.vicl.services.exceptions;

public class NotFoundTaskException extends Exception{
    public NotFoundTaskException(String message) {
        super(message);
    }
    public NotFoundTaskException(){
        super("Not found Task");
    }
}
