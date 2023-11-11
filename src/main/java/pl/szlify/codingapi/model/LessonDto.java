package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LessonDto {
    private Long id;
    @NotBlank(message = "StudentId cannot be empty")
    private Long studentId;
    @NotBlank(message = "TeacherId cannot be empty")
    private Long teacherId;
    @NotBlank(message = "The date cannot be empty")
    private LocalDateTime date;
}
