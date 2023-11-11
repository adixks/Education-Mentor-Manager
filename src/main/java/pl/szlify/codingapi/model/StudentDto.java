package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StudentDto {
    private Long id;
    @NotBlank(message = "The first name cannot be empty")
    private String firstName;
    @NotBlank(message = "The last name cannot be empty")
    private String lastName;
    @NotBlank(message = "The language cannot be empty")
    private String language;
    private Boolean removed;
    private Long teacherId;
}
