package pl.szlify.codingapi.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class TeacherShortDto {
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
    @Size(min = 1, message = "LIST_TOO_SMALL")
    private Set<String> languages;
}
