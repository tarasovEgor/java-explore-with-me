package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllCommentsByEventIdPublic(long eventId, int from, int size);

    CommentDto getEventCommentByIdPublic(long eventId, long commentId);

    CommentDto saveCommentPrivate(NewCommentDto newCommentDto, long userId, long eventId);

    CommentDto patchCommentByIdPrivate(NewCommentDto newCommentDto, long userId, long commentId);

    void deleteCommentByIdPrivate(long userId, long commentId);

    void deleteEventCommentByIdAdmin(long eventId, long commentId);

}
