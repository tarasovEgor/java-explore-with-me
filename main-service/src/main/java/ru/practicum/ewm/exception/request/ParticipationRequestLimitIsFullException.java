package ru.practicum.ewm.exception.request;

public class ParticipationRequestLimitIsFullException extends RuntimeException {
    public ParticipationRequestLimitIsFullException(String message) {
        super(message);
    }
}
