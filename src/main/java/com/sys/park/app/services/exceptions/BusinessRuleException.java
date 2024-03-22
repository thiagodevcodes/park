package com.sys.park.app.services.exceptions;

import java.util.List;

public class BusinessRuleException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<String> errorMessages;

    public BusinessRuleException(String message, List<String> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}