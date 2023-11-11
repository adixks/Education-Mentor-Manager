package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KursantDto {
    private Long id;
    @NotBlank(message = "Imie nie moze byc puste")
    private String imie;
    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String nazwisko;
    @NotBlank(message = "Jezyk nie moze byc puste")
    private String jezyk;
    private Boolean usuniety;
    private Long nauczycielId;
}
