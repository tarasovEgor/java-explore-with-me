package ru.practicum.ewm.exception;

public class BadRequestMethodException extends RuntimeException {
    public BadRequestMethodException(String message) {
        super(message);
    }
}
