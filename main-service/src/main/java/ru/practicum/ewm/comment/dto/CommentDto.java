package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private User commenter;
    private EventShortDto event;
    private String postedOn;
}
