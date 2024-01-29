package ru.practicum.ewm.exception.event;

public class EventHasNotBeenPublishedYetException extends RuntimeException {
    public EventHasNotBeenPublishedYetException(String message) {
        super(message);
    }
}
