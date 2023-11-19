package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TEACHER")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private Boolean deleted;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<StudentEntity> studentsList;

    @ElementCollection
    private List<String> languages;

    @OneToMany(mappedBy = "teacher")
    private Set<LessonEntity> lessons;
}
