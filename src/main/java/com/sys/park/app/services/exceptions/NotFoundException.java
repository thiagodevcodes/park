package com.sys.park.app.services.exceptions;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NotFoundException(String msg) {
        super(msg);
    }
    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}