package ru.practicum.server.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.practicum.server.exception.InvalidRequestDataDateException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidRequestDataDateException(final InvalidRequestDataDateException e) {
        return new ApiError(
                "400",
                "Bad Request.",
                e.getMessage()
        );
    }

}
