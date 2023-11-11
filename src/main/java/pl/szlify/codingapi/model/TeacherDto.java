package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherDto {
    private Long id;
    @NotBlank(message = "The name cannot be empty")
    private String firstName;
    @NotBlank(message = "The name cannot be empty")
    private String lastName;
    private Boolean removed;
    @NotEmpty(message = "Teacher must know programming language")
    private List<String> languages;
    private List<Long> studentsList;
}
