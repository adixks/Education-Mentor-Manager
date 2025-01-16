package pl.szlify.codingapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.repository.LanguageRepository;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-dev.properties")
@AutoConfigureMockMvc
@Transactional
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql")
//@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private String teacherAuthHeader;
    private String studentAuthHeader;

    @BeforeEach
    public void setupDatabase() {
        objectMapper.registerModule(new JavaTimeModule());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        LanguageEntity language = new LanguageEntity()
                .setName("Java");
        entityManager.persist(language);

        TeacherEntity teacher = new TeacherEntity()
                .setFirstName("Jan")
                .setLastName("Smith")
                .setUsername("j.smith")
                .setPassword(passwordEncoder.encode("teacherPassword"))
                .setLanguages(new HashSet<>());
        teacher.getLanguages().add(language);
        entityManager.persist(teacher);

        StudentEntity student = new StudentEntity()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setUsername("a.kowalski")
                .setPassword(passwordEncoder.encode("studentPassword"))
                .setLanguage(language)
                .setTeacher(teacher);
        entityManager.persist(student);

        LessonEntity lesson = new LessonEntity()
                .setDate(LocalDateTime.now().plusYears(10L))
                .setTeacher(teacher)
                .setStudent(student);
        entityManager.persist(lesson);

        entityManager.flush();

        teacherAuthHeader = "Basic " + Base64.getEncoder().encodeToString("j.smith:teacherPassword".getBytes());
        studentAuthHeader = "Basic " + Base64.getEncoder().encodeToString("a.kowalski:studentPassword".getBytes());
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
    public void addLanguage_ReturnsNewLanguage() throws Exception {
        LanguageShortDto languageShortDto = new LanguageShortDto().setName("Python");

        mockMvc.perform(post("/api/v1/language")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", teacherAuthHeader)
                        .content(objectMapper.writeValueAsString(languageShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Python"));

        List<LanguageEntity> languages = languageRepository.findAll();
        assertEquals(2, languages.size());
    }

    @Test
    public void deleteLanguage_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/language/1")
                        .header("Authorization", teacherAuthHeader))
                .andExpect(status().isNoContent());

        Optional<LanguageEntity> language = languageRepository.findById(1L);
        assertTrue(language.isEmpty());
    }

    @Test
    public void getAllLanguage_ReturnsListOfLanguages() throws Exception {
        mockMvc.perform(get("/api/v1/language")
                        .header("Authorization", studentAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Java"));
    }
}
