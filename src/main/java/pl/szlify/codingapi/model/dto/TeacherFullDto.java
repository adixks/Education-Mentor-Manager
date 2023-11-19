package pl.szlify.codingapi.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class TeacherFullDto {
    private Long id;

    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "VALUE_TOO_SMALL")
    private String firstName;

    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "VALUE_TOO_SMALL")
    private String lastName;

    private Boolean removed;

    //    @NotEmptyFields
    @NotNull(message = "NULL_VALUE")
    @Size(min = 1, message = "LIST_TOO_SMALL")
    private List<String> languages;

    private Set<Long> studentsListIds;

    private Set<Long> lessonIds;
}
