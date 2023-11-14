package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.model.LessonEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    Optional<LessonEntity> findByTeacherEntityIdAndDate(Long teacherId, LocalDateTime date);
    Optional<LessonEntity> findByTeacherEntityId(Long teacherId);
    Optional<LessonEntity> findByStudentEntityId(Long studentId);
}
