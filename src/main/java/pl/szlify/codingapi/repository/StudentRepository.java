package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.model.StudentEntity;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByIdAndRemovedFalse(Long id);
}
