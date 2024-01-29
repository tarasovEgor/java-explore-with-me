package ru.practicum.ewm.exception.compilation;

public class CompilationDoesNotExistException extends RuntimeException {
    public CompilationDoesNotExistException(String message) {
        super(message);
    }
}
