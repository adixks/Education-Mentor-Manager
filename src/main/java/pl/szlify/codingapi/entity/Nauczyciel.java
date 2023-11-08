package pl.szlify.codingapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Nauczyciel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;
    private String nazwisko;
    private Boolean usuniety;

    @OneToMany(mappedBy = "nauczyciel", cascade = CascadeType.ALL)
    private List<Kursant> listaKursantow;

    @ElementCollection
    private List<String> jezyki;
}
