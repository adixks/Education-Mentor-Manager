package pl.szlify.codingapi.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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
