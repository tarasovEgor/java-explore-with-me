package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Email
    @NotBlank(message = "Email field can't be blank.")
    @Size(min = 6, max = 254, message = "Email can't be shorter than 6 or greater than 254 characters.")
    private String email;

    @NotBlank(message = "Name field can't be null.")
    @Size(min = 2, max = 250, message = "Name can't be shorter than 2 or greater than 250 characters.")
    private String name;
}
