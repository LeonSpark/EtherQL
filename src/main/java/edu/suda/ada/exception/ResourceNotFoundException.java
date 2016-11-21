package edu.suda.ada.exception;

import java.io.IOException;

/**
 * @author leon.
 */
public class ResourceNotFoundException extends IOException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
