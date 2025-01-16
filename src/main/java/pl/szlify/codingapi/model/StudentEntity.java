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
@ToString(exclude = {"version", "password"})
@Accessors(chain = true)
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String firstName;

    private String lastName;

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID")
    private LanguageEntity language;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private TeacherEntity teacher;

    @OneToMany(mappedBy = "student")
    private Set<LessonEntity> lessons;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "STUDENT";

    @Transient
    public void setVersion(int version) {}
}
