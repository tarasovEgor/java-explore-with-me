package ru.practicum.ewm.exception.request;

public class ParticipationRequestDoesNotExistException extends RuntimeException {
    public ParticipationRequestDoesNotExistException(String message) {
        super(message);
    }
}
