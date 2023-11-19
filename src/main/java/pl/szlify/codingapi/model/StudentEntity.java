package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@Entity
@Table(name = "STUDENT")
@SQLDelete(sql = "UPDATE STUDENT SET deleted = true WHERE id = ? AND deleted = false")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String language;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private TeacherEntity teacher;

    @OneToMany(mappedBy = "student")
    private Set<LessonEntity> lessons;
}
