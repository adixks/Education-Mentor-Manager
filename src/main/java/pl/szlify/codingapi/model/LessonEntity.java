package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "LESSON")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private TeacherEntity teacher;

    private LocalDateTime date;
}
