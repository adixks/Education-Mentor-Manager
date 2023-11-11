package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherBasicInfoDto {
    private Long id;
    @NotBlank(message = "The first name cannot be empty")
    private String firstName;
    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String lastName;
    @NotEmpty(message = "The last name cannot be empty")
    private List<String> languages;
}