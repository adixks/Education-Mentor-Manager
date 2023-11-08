package pl.szlify.codingapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Lekcja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "KURSANT_ID")
    private Kursant kursant;

    @ManyToOne
    @JoinColumn(name = "NAUCZYCIEL_ID")
    private Nauczyciel nauczyciel;

    private LocalDateTime termin;
}
