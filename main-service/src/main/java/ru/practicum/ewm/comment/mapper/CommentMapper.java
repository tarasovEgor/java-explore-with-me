package ru.practicum.ewm.comment.mapper;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static Comment toComment(String text, Event event, User commenter, LocalDateTime localDateTime) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String postedOn = localDateTime.format(formatter);

        return new Comment(
                text,
                event,
                commenter,
                postedOn
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getUser(),
                EventMapper.toEventShortDto(comment.getEvent()),
                comment.getPostedOn()
        );
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        List<CommentDto> dtos= new ArrayList<>();
        for (Comment c : comments) {
            dtos.add(toCommentDto(c));
        }
        return dtos;
    }

}
