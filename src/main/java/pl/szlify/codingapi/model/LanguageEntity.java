package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Table(name = "LANGUAGE")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "version")
@Accessors(chain = true)
public class LanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String name;

    @OneToMany(mappedBy = "language")
    private Set<StudentEntity> students;

    @ManyToMany(mappedBy = "languages")
    private Set<TeacherEntity> teachers;

    @Transient
    public void setVersion(int version) {}
}
