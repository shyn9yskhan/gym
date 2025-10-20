package com.shyn9yskhan.training_type_service.service.exception;

public class TrainingTypeCreationException extends RuntimeException {
    public TrainingTypeCreationException(String message) {
        super(message);
    }

    public TrainingTypeCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
