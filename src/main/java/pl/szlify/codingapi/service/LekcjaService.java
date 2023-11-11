package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.model.KursantEntity;
import pl.szlify.codingapi.model.LekcjaEntity;
import pl.szlify.codingapi.model.NauczycielEntity;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LekcjaMapper;
import pl.szlify.codingapi.model.LekcjaDto;
import pl.szlify.codingapi.repository.KursantRepository;
import pl.szlify.codingapi.repository.LekcjaRepository;
import pl.szlify.codingapi.repository.NauczycielRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LekcjaService {
    private final LekcjaRepository lekcjaRepository;
    private final NauczycielRepository nauczycielRepository;
    private final KursantRepository kursantRepository;
    private final LekcjaMapper lekcjaMapper;

    public List<LekcjaDto> pobierzWszystkieLekcje() {
        return lekcjaRepository.findAll().stream()
                .map(lekcjaMapper::fromDtoToEntity)
                .collect(Collectors.toList());
    }

    public LekcjaDto pobierzLekcje(Long id) {
        return lekcjaRepository.findById(id)
                .map(lekcjaMapper::fromDtoToEntity)
                .orElseThrow(BrakLekcjiException::new);
    }

    public LekcjaDto stworzLekcje(LekcjaDto lekcjaDto) {
        if (lekcjaDto.getTermin().isBefore(LocalDateTime.now())) {
            throw new LekcjaWPrzeszlosciException();
        }

        NauczycielEntity nauczycielEntity = nauczycielRepository
                .findByIdAndUsunietyFalse(lekcjaDto.getNauczycielId()).orElseThrow(BrakNauczycielaException::new);

        KursantEntity kursantEntity = kursantRepository
                .findByIdAndUsunietyFalse(lekcjaDto.getKursantId()).orElseThrow(BrakKursantaException::new);

        if (!kursantEntity.getNauczycielEntity().getId().equals(lekcjaDto.getNauczycielId())) {
            throw new ToNieTwojNauczycielException();
        }

        Optional<LekcjaEntity> existingLekcja = lekcjaRepository.findByNauczycielEntityIdAndTermin(lekcjaDto.getNauczycielId(), lekcjaDto.getTermin());
        if (existingLekcja.isPresent()) {
            throw new ZajetyTerminLekcjiException();
        }

        LekcjaEntity lekcjaEntity = lekcjaMapper.fromEntityToDto(lekcjaDto);
        lekcjaEntity.setNauczycielEntity(nauczycielEntity);
        lekcjaEntity.setKursantEntity(kursantEntity);
        lekcjaRepository.save(lekcjaEntity);
        return lekcjaMapper.fromDtoToEntity(lekcjaEntity);
    }

    public LekcjaDto aktualizujCalaLekcje(Long id, LekcjaDto lekcjaDto) {
        if (lekcjaDto.getTermin().isBefore(LocalDateTime.now())) {
            throw new LekcjaWPrzeszlosciException();
        }

        NauczycielEntity nauczycielEntity = nauczycielRepository
                .findByIdAndUsunietyFalse(lekcjaDto.getNauczycielId()).orElseThrow(BrakNauczycielaException::new);

        KursantEntity kursantEntity = kursantRepository
                .findByIdAndUsunietyFalse(lekcjaDto.getKursantId()).orElseThrow(BrakKursantaException::new);

        if (!kursantEntity.getNauczycielEntity().getId().equals(lekcjaDto.getNauczycielId())) {
            throw new ToNieTwojNauczycielException();
        }

        Optional<LekcjaEntity> existingLekcja = lekcjaRepository.findByNauczycielEntityIdAndTermin(lekcjaDto.getNauczycielId(), lekcjaDto.getTermin());
        if (existingLekcja.isPresent()) {
            throw new ZajetyTerminLekcjiException();
        }

        LekcjaEntity lekcjaEntity = new LekcjaEntity()
                .setId(id)
                .setNauczycielEntity(nauczycielEntity)
                .setKursantEntity(kursantEntity)
                .setTermin(lekcjaDto.getTermin());

        lekcjaRepository.save(lekcjaEntity);
        return lekcjaMapper.fromDtoToEntity(lekcjaEntity);
    }

    public LekcjaDto aktualizujLekcje(Long id, LocalDateTime localDateTime) {
        LekcjaEntity lekcjaEntity = lekcjaRepository.findById(id)
                .orElseThrow(BrakLekcjiException::new);

        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new LekcjaWPrzeszlosciException();
        }

        Optional<LekcjaEntity> existingLekcja = lekcjaRepository.findByNauczycielEntityIdAndTermin(lekcjaEntity.getNauczycielEntity().getId(), localDateTime);
        if (existingLekcja.isPresent()) {
            throw new ZajetyTerminLekcjiException();
        }

        lekcjaEntity.setTermin(localDateTime);
        lekcjaRepository.save(lekcjaEntity);
        return lekcjaMapper.fromDtoToEntity(lekcjaEntity);
    }

    public void usunLekcje(Long id) {
        LekcjaEntity lekcjaEntity = lekcjaRepository.findById(id)
                .orElseThrow(BrakLekcjiException::new);

        if (lekcjaEntity.getTermin().isBefore(LocalDateTime.now())) {
            throw new OdbytaLekcjaException();
        }
        lekcjaRepository.deleteById(id);
    }
}
