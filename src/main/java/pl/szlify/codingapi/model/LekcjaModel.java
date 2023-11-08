package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LekcjaModel {
    private Long id;
    @NotBlank(message = "KursantId nie moze byc puste")
    private Long kursantId;
    @NotBlank(message = "NauczycielId nie moze byc puste")
    private Long nauczycielId;
    @NotBlank(message = "Termin nie moze byc puste")
    private LocalDateTime termin;
}
