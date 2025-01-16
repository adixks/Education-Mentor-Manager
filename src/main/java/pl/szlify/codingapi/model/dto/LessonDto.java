package pl.szlify.codingapi.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
