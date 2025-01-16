package pl.szlify.codingapi.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LessonDateDto {
    //    @NotDateInPast
    @NotNull(message = "NULL_VALUE")
    @FutureOrPresent(message = "PAST_VALUE")
    private LocalDateTime date;
}
