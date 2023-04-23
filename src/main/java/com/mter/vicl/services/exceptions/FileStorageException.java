package com.mter.vicl.services.exceptions;

import java.io.IOException;
import java.nio.file.FileSystemException;

public class FileStorageException extends FileSystemException {
    public FileStorageException(String message) {
        super(message);
    }
}