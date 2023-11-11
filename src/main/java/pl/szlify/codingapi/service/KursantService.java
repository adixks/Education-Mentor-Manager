package pl.szlify.codingapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.exceptions.BrakKursantaException;
import pl.szlify.codingapi.exceptions.BrakNauczycielaException;
import pl.szlify.codingapi.exceptions.ZlyJezykException;
import pl.szlify.codingapi.model.KursantNajwInfoDto;
import pl.szlify.codingapi.repository.KursantRepository;
import pl.szlify.codingapi.repository.NauczycielRepository;
import pl.szlify.codingapi.model.KursantEntity;
import pl.szlify.codingapi.model.NauczycielEntity;
import pl.szlify.codingapi.mapper.KursantMapper;
import pl.szlify.codingapi.model.KursantDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KursantService {
    private final KursantRepository kursantRepository;
    private final NauczycielRepository nauczycielRepository;
    private final KursantMapper kursantMapper;

    public List<KursantNajwInfoDto> pobierzKursantow() {
        return kursantRepository.findAll().stream()
                .map(kursantMapper::fromEntityToKursantNajwInfoDto)
                .collect(Collectors.toList());
    }

    public KursantDto pobierzKursanta(Long id) {
        KursantEntity kursantEntity = kursantRepository.findById(id)
                .orElseThrow(BrakKursantaException::new);
        return kursantMapper.fromEntityToDto(kursantEntity);
    }

    public KursantNajwInfoDto dodajKursanta(KursantNajwInfoDto kursantNajwInfoDto) {
        NauczycielEntity nauczycielEntity = nauczycielRepository.findByIdAndUsunietyFalse(kursantNajwInfoDto.getNauczycielId())
                .orElseThrow(BrakNauczycielaException::new);
        if (!nauczycielEntity.getJezyki().contains(kursantNajwInfoDto.getJezyk())) {
            throw new ZlyJezykException();
        }
        KursantEntity kursantEntity = kursantMapper.fromKursantNajwInfoDtoToEntity(kursantNajwInfoDto);
        kursantEntity.setNauczycielEntity(nauczycielEntity);
        return kursantMapper.fromEntityToKursantNajwInfoDto(kursantRepository.save(kursantEntity));
    }

    public KursantDto aktualizujCalegoKursanta(Long id, KursantNajwInfoDto kursantNajwInfoDto) {
        KursantEntity kursantEntity = kursantRepository.findById(id)
                .orElseThrow(BrakKursantaException::new);

        NauczycielEntity nauczycielEntity = nauczycielRepository.findByIdAndUsunietyFalse(kursantNajwInfoDto.getNauczycielId())
                .orElseThrow(BrakNauczycielaException::new);

        if (!nauczycielEntity.getJezyki().contains(kursantEntity.getJezyk())) {
            throw new ZlyJezykException();
        }

        KursantEntity updatedKursantEntity = kursantMapper.fromNajwInfoAndEntityToEntity(kursantEntity, kursantNajwInfoDto);
        updatedKursantEntity.setNauczycielEntity(nauczycielEntity);
        return kursantMapper.fromEntityToDto(kursantRepository.save(updatedKursantEntity));
    }

    public KursantNajwInfoDto aktualizujKursanta(Long id, Long nauczycielId) {
        KursantEntity kursantEntity = kursantRepository.findById(id)
                .orElseThrow(BrakKursantaException::new);
        NauczycielEntity nauczycielEntity = nauczycielRepository.findByIdAndUsunietyFalse(nauczycielId)
                .orElseThrow(BrakNauczycielaException::new);
        if (!nauczycielEntity.getJezyki().contains(kursantEntity.getJezyk())) {
            throw new ZlyJezykException();
        }
        kursantEntity.setNauczycielEntity(nauczycielEntity);
        kursantRepository.save(kursantEntity);
        return kursantMapper.fromEntityToKursantNajwInfoDto(kursantEntity);
    }

    public void usunKursanta(Long id) {
        kursantRepository.deleteById(id);
    }
}
