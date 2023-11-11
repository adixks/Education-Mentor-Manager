package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class KursantNajwInfoDto {
    private Long id;
    @NotBlank(message = "Imie nie moze byc puste")
    private String imie;
    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String nazwisko;
    @NotEmpty(message = "Kursant musi chciec uczyc sie jezyka")
    private String jezyk;
    @NotEmpty(message = "Kursant musi miec nauczyciela")
    private Long nauczycielId;
}