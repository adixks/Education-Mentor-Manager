package pl.szlify.codingapi.model;

import lombok.*;
import lombok.experimental.Accessors;
import pl.szlify.codingapi.validator.NotDateInPast;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LessonDateDto {
    @NotDateInPast
    private LocalDateTime date;
}
