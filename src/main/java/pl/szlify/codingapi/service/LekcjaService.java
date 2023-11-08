package pl.szlify.codingapi.service;

import org.springframework.stereotype.Service;
import pl.szlify.codingapi.entity.Kursant;
import pl.szlify.codingapi.entity.Lekcja;
import pl.szlify.codingapi.entity.Nauczyciel;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LekcjaMapper;
import pl.szlify.codingapi.model.LekcjaModel;
import pl.szlify.codingapi.repository.KursantRepository;
import pl.szlify.codingapi.repository.LekcjaRepository;
import pl.szlify.codingapi.repository.NauczycielRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LekcjaService {
    private LekcjaRepository lekcjaRepository;
    private NauczycielRepository nauczycielRepository;
    private KursantRepository kursantRepository;
    private LekcjaMapper lekcjaMapper;

    public LekcjaService(LekcjaRepository lekcjaRepository, NauczycielRepository nauczycielRepository, KursantRepository kursantRepository, LekcjaMapper lekcjaMapper) {
        this.lekcjaRepository = lekcjaRepository;
        this.nauczycielRepository = nauczycielRepository;
        this.kursantRepository = kursantRepository;
        this.lekcjaMapper = lekcjaMapper;
    }

    public List<LekcjaModel> pobierzWszystkieLekcje() {
        return lekcjaRepository.findAll().stream()
                .map(lekcjaMapper::from)
                .collect(Collectors.toList());
    }

    public LekcjaModel pobierzLekcje(Long id) {
        return lekcjaRepository.findById(id)
                .map(lekcjaMapper::from)
                .orElseThrow(BrakLekcjiException::new);
    }

    public LekcjaModel stworzLekcje(LekcjaModel lekcjaModel) {
        if (lekcjaModel.getTermin().isBefore(LocalDateTime.now())) {
            throw new LekcjaWPrzeszlosciException();
        }

        Optional<Kursant> optionalKursant = kursantRepository.findById(lekcjaModel.getKursantId());
        Kursant kursant = optionalKursant.orElse(new Kursant());
        if (!kursant.getNauczyciel().getId().equals(lekcjaModel.getNauczycielId()))
        {
            throw new ToNieTwojNauczycielException();
        }

        List<Lekcja> existingLekcje = lekcjaRepository.findByNauczycielId(lekcjaModel.getNauczycielId());
        for (Lekcja existingLekcja : existingLekcje) {
            if (existingLekcja.getTermin().isEqual(lekcjaModel.getTermin())) {
                throw new ZajetyTerminLekcjiException();
            }
        }

        Lekcja lekcja = lekcjaMapper.from(lekcjaModel);
        Optional<Nauczyciel> optionalNauczyciel = nauczycielRepository.findById(lekcjaModel.getNauczycielId());
        Nauczyciel nauczyciel = optionalNauczyciel.orElse(new Nauczyciel());

        if (kursant.getUsuniety() || nauczyciel.getUsuniety()) {
            throw new ElementUsunietyException();
        }

        lekcja.setNauczyciel(nauczyciel);
        lekcja.setKursant(kursant);
        lekcjaRepository.save(lekcja);
        return lekcjaMapper.from(lekcja);
    }

    public LekcjaModel aktualizujLekcje(Long id, LocalDateTime localDateTime) {
        Lekcja lekcja = lekcjaRepository.findById(id)
                .orElseThrow(BrakLekcjiException::new);

        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new LekcjaWPrzeszlosciException();
        }

        List<Lekcja> existingLekcje = lekcjaRepository.findByNauczycielId(lekcja.getNauczyciel().getId());
        for (Lekcja existingLekcja : existingLekcje) {
            if (existingLekcja.getTermin().isEqual(localDateTime) && !existingLekcja.getId().equals(id)) {
                throw new ZajetyTerminLekcjiException();
            }
        }

        lekcja.setTermin(localDateTime);
        lekcjaRepository.save(lekcja);
        return lekcjaMapper.from(lekcja);
    }

    public void usunLekcje(Long id) {
        Lekcja lekcja = lekcjaRepository.findById(id)
                .orElseThrow(BrakLekcjiException::new);

        if (lekcja.getTermin().isBefore(LocalDateTime.now())) {
            throw new OdbytaLekcjaException();
        }
        lekcjaRepository.delete(lekcja);
    }
}
