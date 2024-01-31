package ru.practicum.ewm.comment.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.InvalidMethodException;
import ru.practicum.ewm.exception.comment.CommentDoesNotExistException;
import ru.practicum.ewm.exception.event.EventDoesNotExistException;
import ru.practicum.ewm.exception.event.InvalidEventStateException;
import ru.practicum.ewm.exception.user.UserDoesNotExistException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              EventRepository eventRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CommentDto> getAllCommentsByEventIdPublic(long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventDoesNotExistException("Event doesn't exist."));

        List<Comment> comments = commentRepository
                .findAllByEvent(event, PageRequest.of(from, size)).getContent();

        return CommentMapper.toCommentDto(comments);
    }

    @Override
    public CommentDto getEventCommentByIdPublic(long eventId, long commentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventDoesNotExistException("Event doesn't exist."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException("Comment doesn't exist."));

        if (!comment.getEvent().getId().equals(event.getId())) {
            throw new CommentDoesNotExistException("Comment doesn't exist.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new InvalidEventStateException("Event is not in the right state.");
        }

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto saveCommentPrivate(NewCommentDto newCommentDto, long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User doesn't exist."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventDoesNotExistException("Event doesn't exist."));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new InvalidEventStateException("Event is not in the right state.");
        }

        Comment comment = commentRepository
                .save(CommentMapper
                        .toComment(
                                newCommentDto.getText(),
                                event, user,
                                LocalDateTime.now()
                        )
                );

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto patchCommentByIdPrivate(NewCommentDto newCommentDto, long userId, long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User doesn't ecist."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException("Comment doesn't exist."));

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new InvalidMethodException("Method not allowed.");
        }
        if (newCommentDto.getText() != null) {
            comment.setText(newCommentDto.getText());
        }

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByIdPrivate(long userId, long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User doesn't exist."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException("Comment doesn't exist."));

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new InvalidMethodException("Method not allowed.");
        }
        commentRepository.delete(comment);
    }

    @Override
    public void deleteEventCommentByIdAdmin(long eventId, long commentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventDoesNotExistException("Event doesn't exist."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentDoesNotExistException("Comment doesn't exist."));

        if (!event.getId().equals(comment.getEvent().getId())) {
            throw new InvalidMethodException("Method not allowed.");
        }
        commentRepository.delete(comment);
    }
}
