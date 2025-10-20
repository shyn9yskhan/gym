package com.shyn9yskhan.trainer_service.service.exception;

public class TrainerCreationException extends RuntimeException {
    public TrainerCreationException(String message) {
        super(message);
    }

    public TrainerCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
