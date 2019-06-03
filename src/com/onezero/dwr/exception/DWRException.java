package com.onezero.dwr.exception;

public class DWRException extends RuntimeException {
    private final String errorType;
    private final String javaClassName;
    private final String fingerprint;
    private final String errorId;
    private final String timestamp;

    public DWRException(String errorType, String javaClassName, String fingerprint, String errorId, String message,
        String timestamp) {
        super(message);
        this.errorType = errorType;
        this.javaClassName = javaClassName;
        this.fingerprint = fingerprint;
        this.errorId = errorId;
        this.timestamp = timestamp;
    }

}
