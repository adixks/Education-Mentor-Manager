package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "Nauczyciel")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class NauczycielEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;
    private String nazwisko;
    private Boolean usuniety;

    @OneToMany(mappedBy = "nauczycielEntity", cascade = CascadeType.ALL)
    private List<KursantEntity> listaKursantow;

    @ElementCollection
    private List<String> jezyki;
}
