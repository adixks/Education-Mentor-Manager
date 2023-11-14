package pl.szlify.codingapi.model;

import lombok.Data;
import lombok.experimental.Accessors;
import pl.szlify.codingapi.validator.NotEmptyFields;

@Data
@Accessors(chain = true)
public class StudentDto {
    private Long id;

    @NotEmptyFields
    private String firstName;

    @NotEmptyFields
    private String lastName;

    @NotEmptyFields
    private String language;

    private Boolean removed;
    private Long teacherId;
}
