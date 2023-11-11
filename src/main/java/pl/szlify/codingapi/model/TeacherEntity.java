package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "Teacher")
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
    private Boolean removed;

    @OneToMany(mappedBy = "teacherEntity", cascade = CascadeType.ALL)
    private List<StudentEntity> studentsList;

    @ElementCollection
    private List<String> languages;
}
