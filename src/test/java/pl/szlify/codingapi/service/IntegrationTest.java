package pl.szlify.codingapi.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.model.dto.TeacherLanguagesDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.repository.LanguageRepository;
import pl.szlify.codingapi.repository.TeacherRepository;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql")
//@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void setupDatabase() {
        LanguageEntity language = new LanguageEntity();
        language.setName("Java");
        entityManager.persist(language);

        LessonEntity lesson = new LessonEntity();
        lesson.setDate(LocalDateTime.now());
        entityManager.persist(lesson);

        StudentEntity student = new StudentEntity();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setLanguage(language);
        entityManager.persist(student);

        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setLanguages(new HashSet<>());
        teacher.getLanguages().add(language);
        entityManager.persist(teacher);

        entityManager.flush();
    }

    @AfterEach
    public void cleanupDatabase() {
        entityManager.createNativeQuery("DELETE FROM LESSON").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE LESSON AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM STUDENT").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE STUDENT AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM TEACHER_LANGUAGE").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM TEACHER").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE TEACHER AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM LANGUAGE").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE LANGUAGE AUTO_INCREMENT = 1").executeUpdate();
    }

    @Test
    public void getAllLanguage_ReturnsListOfLanguages() throws Exception {
        mockMvc.perform(get("/api/v1/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Java"));
    }

    @Test
    public void deleteLanguage_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/language/1"))
                .andExpect(status().isNoContent());

        Optional<LanguageEntity> language = languageRepository.findById(1L);
        assertTrue(language.isEmpty());

    }

    @Test
    public void addLanguage_ReturnsNewLanguage() throws Exception {
        LanguageShortDto languageShortDto = new LanguageShortDto();
        languageShortDto.setName("Python");

        mockMvc.perform(post("/api/v1/language")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(languageShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Python"));

        List<LanguageEntity> languages = languageRepository.findAll();
        assertEquals(2, languages.size());
    }

    @Test
    public void getTeachersList_ReturnsListOfTeachers() throws Exception {
        mockMvc.perform(get("/api/v1/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Jane"));
    }

    @Test
    public void getTeacher_ReturnsTeacher() throws Exception {
        mockMvc.perform(get("/api/v1/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    public void addTeacher_ReturnsNewTeacher() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto();
        teacherShortDto.setFirstName("John");
        teacherShortDto.setLastName("Doe");
        teacherShortDto.setLanguages(new HashSet<>() {{
            add("Java");
        }});

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertEquals(2, teachers.size());
    }

    @Test
    public void updateEntireTeacher_ReturnsUpdatedTeacher() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto();
        teacherShortDto.setFirstName("John");
        teacherShortDto.setLastName("Doe");
        teacherShortDto.setLanguages(new HashSet<>() {{
            add("Java");
        }});

        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertEquals(1, teachers.size());
    }

    @Test
    public void updateTeacherLanguagesList_ReturnsUpdatedTeacher() throws Exception {
        TeacherLanguagesDto teacherLanguagesDto = new TeacherLanguagesDto();
        teacherLanguagesDto.setLanguagesList(Arrays.asList("Java", "Python"));

        mockMvc.perform(patch("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherLanguagesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.languages[0]").value("Java"))
                .andExpect(jsonPath("$.languages[1]").value("Python"));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertEquals(1, teachers.size());
    }


}
