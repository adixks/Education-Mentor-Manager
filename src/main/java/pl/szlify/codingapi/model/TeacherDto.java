package pl.szlify.codingapi.model;

import lombok.Data;
import lombok.experimental.Accessors;
import pl.szlify.codingapi.validator.NotEmptyFields;

import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherDto {
    private Long id;
    @NotEmptyFields
    private String firstName;
    @NotEmptyFields
    private String lastName;
    private Boolean removed;
    @NotEmptyFields
    private List<String> languages;
    private List<Long> studentsList;
}
