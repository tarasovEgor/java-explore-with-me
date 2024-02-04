package ru.practicum.ewm.exception.comment;

public class CommentDoesNotExistException extends RuntimeException {
    public CommentDoesNotExistException(String message) {
        super(message);
    }
}
