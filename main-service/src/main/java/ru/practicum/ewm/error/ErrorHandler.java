package ru.practicum.ewm.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.practicum.ewm.exception.BadRequestMethodException;
import ru.practicum.ewm.exception.InvalidMethodException;
import ru.practicum.ewm.exception.category.CategoryDoesNotExistException;
import ru.practicum.ewm.exception.comment.CommentDoesNotExistException;
import ru.practicum.ewm.exception.compilation.CompilationDoesNotExistException;
import ru.practicum.ewm.exception.event.*;
import ru.practicum.ewm.exception.request.InvalidInitiatorException;
import ru.practicum.ewm.exception.request.ParticipationRequestDoesNotExistException;
import ru.practicum.ewm.exception.request.ParticipationRequestLimitIsFullException;
import ru.practicum.ewm.exception.request.RepeatedRequestException;
import ru.practicum.ewm.exception.user.UserDoesNotExistException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidMethodException(final InvalidMethodException e) {
        return new ApiError(
                "409",
                "Conflict.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryDoesNotExistException(final CategoryDoesNotExistException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventDoesNotExistException(final EventDoesNotExistException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationDoesNotExistException(final CompilationDoesNotExistException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidEventDateException(final InvalidEventDateException e) {
        return new ApiError(
                "400",
                "Bad Request.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserDoesNotExistException(final UserDoesNotExistException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidStateActionException(final InvalidStateActionException e) {
        return new ApiError(
                "400",
                "Bad Request.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidEventStateException(final InvalidEventStateException e) {
        return new ApiError(
                "409",
                "Conflict.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleParticipationRequestDoesNotExistException(final ParticipationRequestDoesNotExistException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestMethodException(final BadRequestMethodException e) {
        return new ApiError(
                "400",
                "Bad Request.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationRequestLimitIsFullException(final ParticipationRequestLimitIsFullException e) {
        return new ApiError(
                "409",
                "Conflict.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventHasNotBeenPublishedYetException(final EventHasNotBeenPublishedYetException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRepeatedRequestException(final RepeatedRequestException e) {
        return new ApiError(
                "409",
                "Conflict.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidInitiatorException(final InvalidInitiatorException e) {
        return new ApiError(
                "409",
                "Conflict.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentDoesNotExistException(final CommentDoesNotExistException e) {
        return new ApiError(
                "404",
                "Not Found.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ApiError(
                "400",
                "Bad Request.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        return new ApiError(
                "400",
                "Bad Request.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleThrowable(final Throwable thr) {
        return new ApiError(
                "400",
                "Bad Request.",
                thr.getMessage()
        );
    }
}
