package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LekcjaDto {
    private Long id;
    @NotBlank(message = "KursantId nie moze byc puste")
    private Long kursantId;
    @NotBlank(message = "NauczycielId nie moze byc puste")
    private Long nauczycielId;
    @NotBlank(message = "Termin nie moze byc puste")
    private LocalDateTime termin;
}
