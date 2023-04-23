package com.mter.vicl.services.exceptions;

import java.io.IOException;

public class NotFoundFileException extends FileStorageException {
    public NotFoundFileException(String message) {
        super(message);
    }
}
