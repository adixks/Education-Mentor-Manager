package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class NauczycielModel {
    private Long id;
    @NotBlank(message = "Imie nie moze byc puste")
    private String imie;
    @NotBlank(message = "Nazwisko nie moze byc puste")
    private String nazwisko;
    private Boolean usuniety;
    @NotEmpty(message = "Nauczyciel musi umiec jezyk")
    private List<String> jezyki;
    private List<Long> listaKursantow;
}
