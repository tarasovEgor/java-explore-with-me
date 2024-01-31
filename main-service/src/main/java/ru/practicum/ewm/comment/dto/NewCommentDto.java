package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Can't create a comment with blank text.")
    @Size(min = 10, max = 5000, message = "Comment can't be shorter than 20 or longer than 5000 characters.")
    private String text;
}
