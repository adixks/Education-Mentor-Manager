package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@Entity
@Table(name = "TEACHER")
@SQLDelete(sql = "UPDATE TEACHER SET deleted = true WHERE id = ? AND deleted = false")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "version")
@Accessors(chain = true)
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String firstName;

    private String lastName;

    private Boolean deleted;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<StudentEntity> studentsList;

    @OneToMany(mappedBy = "teacher")
    private Set<LessonEntity> lessons;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "TEACHER_LANGUAGE",
            joinColumns = @JoinColumn(name = "TEACHER_ID"),
            inverseJoinColumns = @JoinColumn(name = "LANGUAGE_ID"))
    private Set<LanguageEntity> languages;

    @Transient
    public void setVersion(int version) {}
}
