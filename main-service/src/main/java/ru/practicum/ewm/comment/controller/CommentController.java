package ru.practicum.ewm.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllCommentsByEventIdPublic(@PathVariable long eventId,
                                                          @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                          @Positive @RequestParam(defaultValue = "10") int size) {
        return commentService.getAllCommentsByEventIdPublic(eventId, from, size);
    }

    @GetMapping("/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getEventCommentByIdPublic(@PathVariable long eventId,
                                                @PathVariable long commentId) {
        return commentService.getEventCommentByIdPublic(eventId, commentId);
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto saveCommentPrivate(@Valid @RequestBody NewCommentDto newCommentDto,
                                  @PathVariable long userId,
                                  @PathVariable long eventId) {
        return commentService.saveCommentPrivate(newCommentDto, userId, eventId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto patchCommentByIdPrivate(@Valid @RequestBody NewCommentDto newCommentDto,
                                              @PathVariable long userId,
                                              @PathVariable long commentId) {
        return commentService.patchCommentByIdPrivate(newCommentDto, userId, commentId);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByIdPrivate(@PathVariable long userId,
                                         @PathVariable long commentId) {
        commentService.deleteCommentByIdPrivate(userId, commentId);
    }

    @DeleteMapping("/admin/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventCommentByIdAdmin(@PathVariable long eventId,
                                            @PathVariable long commentId) {
        commentService.deleteEventCommentByIdAdmin(eventId, commentId);
    }

}
