package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.entity.Kursant;

public interface KursantRepository extends JpaRepository<Kursant, Long> {
}
