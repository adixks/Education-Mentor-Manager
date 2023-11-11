package pl.szlify.codingapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.exceptions.LackofTeacherException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void getTeachersList_shouldReturnListOfTeacherBasicInfoDto() {
        // Given
        TeacherEntity teacherOne = new TeacherEntity();
        TeacherEntity teacherTwo = new TeacherEntity();
        List<TeacherEntity> teachers = Arrays.asList(teacherOne, teacherTwo);

        TeacherBasicInfoDto teacherBasicInfoOne = new TeacherBasicInfoDto();
        TeacherBasicInfoDto teacherBasicInfoTwo = new TeacherBasicInfoDto();
        List<TeacherBasicInfoDto> teachersBasicInfoList = Arrays.asList(teacherBasicInfoOne, teacherBasicInfoTwo);
        when(teacherRepository.findAll()).thenReturn(teachers);
        when(teacherMapper.fromEntityToNajwInfoDto(teacherOne)).thenReturn(teacherBasicInfoOne);
        when(teacherMapper.fromEntityToNajwInfoDto(teacherTwo)).thenReturn(teacherBasicInfoTwo);

        // When
        List<TeacherBasicInfoDto> result = teacherService.getTeachersList();

        // Then
        assertEquals(teachersBasicInfoList, result);
        verify(teacherRepository, times(1)).findAll();
        verify(teacherMapper, times(1)).fromEntityToNajwInfoDto(teacherOne);
        verify(teacherMapper, times(1)).fromEntityToNajwInfoDto(teacherTwo);
    }

    @Test
    void getTeacher_shouldReturnTeacherBasicInfoDto() {
        // Given
        Long id = 1L;
        TeacherEntity teacherEntity = new TeacherEntity();
        TeacherDto teacherDto = new TeacherDto();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.fromEntityToDto(teacherEntity)).thenReturn(teacherDto);

        // When
        TeacherDto result = teacherService.getTeacher(id);

        // Then
        assertNotNull(result);
        assertEquals(teacherDto, result);
        verify(teacherRepository, times(1)).findById(id);
    }

    @Test
    void addTeacher_shouldReturnLackofTeacherException() {
        // Given
        Long id = 1L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(LackofTeacherException.class, () -> teacherService.getTeacher(id));

        // Then
        verify(teacherRepository, times(1)).findById(id);
    }

    @Test
    void updateEntireTeacher_shouldReturnTeacherBasicInfoDto() {
        // Given
        Long nauczycielId = 1L;
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto();
        TeacherEntity teacherEntity = new TeacherEntity();
        TeacherEntity updatedTeacherEntity = new TeacherEntity();
        when(teacherRepository.findById(nauczycielId)).thenReturn(Optional.of(teacherEntity));
        when(teacherRepository.save(any(TeacherEntity.class))).thenReturn(updatedTeacherEntity);
        when(teacherMapper.fromNajwInfoAndEntityToEntity(any(TeacherEntity.class), eq(teacherBasicInfoDto)))
                .thenReturn(updatedTeacherEntity);
        when(teacherMapper.fromEntityToNajwInfoDto(updatedTeacherEntity)).thenReturn(teacherBasicInfoDto);

        // When
        TeacherBasicInfoDto result = teacherService.updateEntireTeacher(nauczycielId, teacherBasicInfoDto);

        // Then
        assertEquals(teacherBasicInfoDto, result);
        verify(teacherRepository, times(1)).findById(nauczycielId);
        verify(teacherRepository, times(1)).save(updatedTeacherEntity);
        verify(teacherMapper, times(1)).fromNajwInfoAndEntityToEntity(teacherEntity, teacherBasicInfoDto);
        verify(teacherMapper, times(1)).fromEntityToNajwInfoDto(updatedTeacherEntity);
    }

    @Test
    void updateTeacherLanguagesList_shouldReturnLackofTeacherException() {
        // Given
        Long id = 1L;
        List<String> listaJezykow = Arrays.asList("java", "C");
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(LackofTeacherException.class, () -> teacherService.updateTeacherLanguagesList(id, listaJezykow));

        // Then
        verify(teacherRepository, times(1)).findById(id);
        verify(teacherRepository, never()).save(any(TeacherEntity.class));
        verify(teacherMapper, never()).fromEntityToDto(any(TeacherEntity.class));
    }

    @Test
    void deleteTeacher_shouldReturn200() {
        // Given
        Long id = 1L;
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(id);
        teacherEntity.setRemoved(false);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherRepository.save(any(TeacherEntity.class))).thenReturn(teacherEntity);

        // When
        assertDoesNotThrow(() -> teacherService.deleteTeacher(id));

        // Then
        assertTrue(teacherEntity.getRemoved());
        verify(teacherRepository, times(1)).findById(id);
        verify(teacherRepository, times(1)).save(teacherEntity);
    }

    @Test
    void deleteTeacher_shouldReturnLackofTeacherException() {
        // Given
        Long id = 1L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(LackofTeacherException.class, () -> teacherService.deleteTeacher(id));

        // Then
        verify(teacherRepository, times(1)).findById(id);
        verify(teacherRepository, never()).save(any(TeacherEntity.class));
    }
}
