package pl.szlify.codingapi.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LessonDateDto {
//    @NotDateInPast
    @NotNull(message = "NULL_VALUE")
    @FutureOrPresent(message = "PAST_VALUE")
    private LocalDateTime date;
}
