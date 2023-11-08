package pl.szlify.codingapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Kursant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;
    private String nazwisko;
    private String jezyk;
    private Boolean usuniety;

    @ManyToOne
    @JoinColumn(name = "NAUCZYCIEL_ID")
    private Nauczyciel nauczyciel;
}
