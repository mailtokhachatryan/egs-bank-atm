package com.egs.core.bank.exception;


import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GenericException extends RuntimeException {

    protected ExceptionEnum reason;
    protected HttpStatus status;
    protected UUID uuid;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public GenericException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public GenericException(String message, ExceptionEnum reason, HttpStatus status) {
        super(message);
        this.reason = reason;
        this.status = status;
        uuid = UUID.randomUUID();
    }

    public GenericException(Throwable e, ExceptionEnum reason, HttpStatus status) {
        super(e);
        this.reason = reason;
        this.status = status;
        uuid = UUID.randomUUID();
    }

    public GenericException(String message, Throwable e, ExceptionEnum reason, HttpStatus status) {
        super(message, e);
        this.reason = reason;
        this.status = status;
        uuid = UUID.randomUUID();
    }

    public ExceptionEnum getReason() {
        return reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public UUID getUUID() {
        return uuid;
    }
}