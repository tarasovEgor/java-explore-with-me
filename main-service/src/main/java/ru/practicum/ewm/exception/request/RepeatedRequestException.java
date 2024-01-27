package ru.practicum.ewm.exception.request;

public class RepeatedRequestException extends RuntimeException {
    public RepeatedRequestException(String message) {
        super(message);
    }
}
