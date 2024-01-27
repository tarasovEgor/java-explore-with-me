package ru.practicum.ewm.exception.event;

public class EventDoesNotExistException extends RuntimeException {
    public EventDoesNotExistException(String message) {
        super(message);
    }
}
