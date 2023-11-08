package pl.szlify.codingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.codingapi.entity.Lekcja;

import java.util.List;

public interface LekcjaRepository extends JpaRepository<Lekcja, Long> {
    List<Lekcja> findByNauczycielId(Long nauczycielId);
}
