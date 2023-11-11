package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "Kursant")
@SQLDelete(sql = "UPDATE Kursant SET usuniety = true WHERE id = ? AND usuniety = false")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class KursantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;
    private String nazwisko;
    private String jezyk;
    private Boolean usuniety;

    @ManyToOne
    @JoinColumn(name = "NAUCZYCIEL_ID")
    private NauczycielEntity nauczycielEntity;
}
