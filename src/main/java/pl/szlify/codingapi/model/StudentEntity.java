package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "Student")
@SQLDelete(sql = "UPDATE Student SET removed = true WHERE id = ? AND removed = false")
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
    private Boolean removed;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private TeacherEntity teacherEntity;
}
