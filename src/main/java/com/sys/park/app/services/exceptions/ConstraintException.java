package com.sys.park.app.services.exceptions;

import java.util.List;

public class ConstraintException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<String> errorMessages;

    public ConstraintException(String message, List<String> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}