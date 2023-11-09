package pl.szlify.codingapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, message = "Lista języków nie może być pusta")
    private List<String> jezyki;
    private List<Long> listaKursantow;
}
