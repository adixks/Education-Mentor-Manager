package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lekcja")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LekcjaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "KURSANT_ID")
    private KursantEntity kursantEntity;

    @ManyToOne
    @JoinColumn(name = "NAUCZYCIEL_ID")
    private NauczycielEntity nauczycielEntity;

    private LocalDateTime termin;
}
