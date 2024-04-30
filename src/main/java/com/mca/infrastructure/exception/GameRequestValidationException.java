package com.mca.infrastructure.exception;

public class GameRequestValidationException extends RuntimeException {

    public GameRequestValidationException() {
        super("Something happened validating the game");
    }

    public GameRequestValidationException(String message) {
        super(message);
    }

    public GameRequestValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
