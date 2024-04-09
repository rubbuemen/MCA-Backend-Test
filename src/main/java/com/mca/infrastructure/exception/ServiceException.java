package com.mca.infrastructure.exception;

public class ServiceException extends RuntimeException {

    public ServiceException() {
        super("Something happened in the service");
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
