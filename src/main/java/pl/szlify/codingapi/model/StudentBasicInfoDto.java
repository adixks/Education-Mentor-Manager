package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StudentBasicInfoDto {
    private Long id;
    @NotBlank(message = "The first name cannot be empty")
    private String firstName;
    @NotBlank(message = "The last name cannot be empty")
    private String lastName;
    @NotEmpty(message = "The student must want to learn the language")
    private String language;
    @NotEmpty(message = "The student must have a teacher")
    private Long teacherId;
}