package ru.practicum.ewm.error;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(String status,
                    String reason,
                    String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
    }
}
