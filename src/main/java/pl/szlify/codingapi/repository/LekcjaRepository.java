package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.model.LekcjaEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LekcjaRepository extends JpaRepository<LekcjaEntity, Long> {
    Optional<LekcjaEntity> findByNauczycielEntityIdAndTermin(Long nauczycielId, LocalDateTime termin);

}
