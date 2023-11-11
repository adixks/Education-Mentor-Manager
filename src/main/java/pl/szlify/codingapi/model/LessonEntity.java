package pl.szlify.codingapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lesson")
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity studentEntity;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private TeacherEntity teacherEntity;

    private LocalDateTime date;
}
