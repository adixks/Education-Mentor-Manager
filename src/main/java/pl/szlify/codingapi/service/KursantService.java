package pl.szlify.codingapi.service;

import org.springframework.stereotype.Service;
import pl.szlify.codingapi.exceptions.BrakKursantaException;
import pl.szlify.codingapi.exceptions.BrakNauczycielaException;
import pl.szlify.codingapi.exceptions.ZlyJezykException;
import pl.szlify.codingapi.repository.KursantRepository;
import pl.szlify.codingapi.repository.NauczycielRepository;
import pl.szlify.codingapi.entity.Kursant;
import pl.szlify.codingapi.entity.Nauczyciel;
import pl.szlify.codingapi.mapper.KursantMapper;
import pl.szlify.codingapi.model.KursantModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KursantService {
    private KursantRepository kursantRepository;
    private NauczycielRepository nauczycielRepository;
    private KursantMapper kursantMapper;

    public KursantService(KursantRepository kursantRepository, NauczycielRepository nauczycielRepository, KursantMapper kursantMapper) {
        this.kursantRepository = kursantRepository;
        this.nauczycielRepository = nauczycielRepository;
        this.kursantMapper = kursantMapper;
    }

    public List<KursantModel> pobierzKursantow() {
        return kursantRepository.findAll().stream()
                .map(kursantMapper::from)
                .collect(Collectors.toList());
    }

    public KursantModel pobierzKursanta(Long id) {
        Kursant kursant = kursantRepository.findById(id)
                .orElseThrow(BrakKursantaException::new);
        return kursantMapper.from(kursant);
    }

    public KursantModel dodajKursanta(KursantModel studentModel) {
        Nauczyciel nauczyciel = nauczycielRepository.findById(studentModel.getNauczycielId())
                .orElseThrow(BrakNauczycielaException::new);
        if (!nauczyciel.getJezyki().contains(studentModel.getJezyk())) {
            throw new ZlyJezykException();
        }
        Kursant kursant = kursantMapper.from(studentModel);
        kursant.setNauczyciel(nauczyciel);
        kursantRepository.save(kursant);
        return kursantMapper.from(kursant);
    }

    public KursantModel aktualizujKursanta(Long id, Long nauczycielId) {
        Kursant kursant = kursantRepository.findById(id)
                .orElseThrow(BrakKursantaException::new);
        Nauczyciel nauczyciel = nauczycielRepository.findById(nauczycielId)
                .orElseThrow(BrakNauczycielaException::new);
        if (!nauczyciel.getJezyki().contains(kursant.getJezyk())) {
            throw new ZlyJezykException();
        }
        kursant.setNauczyciel(nauczyciel);
        kursantRepository.save(kursant);
        return kursantMapper.from(kursant);
    }

    public void usunKursanta(Long id) {
        Kursant kursant = kursantRepository.findById(id)
                .orElseThrow(BrakKursantaException::new);
        kursant.setUsuniety(true);
        kursantRepository.save(kursant);
    }
}
