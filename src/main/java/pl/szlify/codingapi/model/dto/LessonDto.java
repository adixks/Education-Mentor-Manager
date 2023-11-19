package pl.szlify.codingapi.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LessonDto {
//    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Positive(message = "NEGATIVE_VALUE")
    private Long studentId;

//    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Positive(message = "NEGATIVE_VALUE")
    private Long teacherId;

//    @NotDateInPast
    @NotNull(message = "NULL_VALUE")
    @FutureOrPresent(message = "PAST_VALUE")
    private LocalDateTime date;
}
