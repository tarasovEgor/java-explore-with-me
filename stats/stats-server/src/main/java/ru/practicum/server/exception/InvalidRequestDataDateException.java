package ru.practicum.server.exception;

public class InvalidRequestDataDateException extends RuntimeException {
    public InvalidRequestDataDateException(String message) {
        super(message);
    }
}
