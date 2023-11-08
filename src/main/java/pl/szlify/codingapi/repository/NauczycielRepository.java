package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.entity.Nauczyciel;

public interface NauczycielRepository extends JpaRepository<Nauczyciel, Long> {
}
