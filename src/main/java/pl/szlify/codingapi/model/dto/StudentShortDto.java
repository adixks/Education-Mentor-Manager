package pl.szlify.codingapi.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StudentShortDto {
    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "VALUE_TOO_SMALL")
    private String firstName;

    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "VALUE_TOO_SMALL")
    private String lastName;

    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "VALUE_TOO_SMALL")
    private String language;

    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Positive(message = "NEGATIVE_VALUE")
    private Long teacherId;
}
