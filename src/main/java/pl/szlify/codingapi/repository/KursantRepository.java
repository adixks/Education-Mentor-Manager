package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.model.KursantEntity;
import pl.szlify.codingapi.model.NauczycielEntity;

import java.util.Optional;

public interface KursantRepository extends JpaRepository<KursantEntity, Long> {
    Optional<KursantEntity> findByIdAndUsunietyFalse(Long id);
}
