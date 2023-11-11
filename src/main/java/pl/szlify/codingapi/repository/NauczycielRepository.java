package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.model.NauczycielEntity;

import java.util.Optional;

public interface NauczycielRepository extends JpaRepository<NauczycielEntity, Long> {
    Optional<NauczycielEntity> findByIdAndUsunietyFalse(Long id);
}
