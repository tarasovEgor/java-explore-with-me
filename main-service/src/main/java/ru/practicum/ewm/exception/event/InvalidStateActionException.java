package ru.practicum.ewm.exception.event;

public class InvalidStateActionException extends RuntimeException {
    public InvalidStateActionException(String message) {
        super(message);
    }
}
