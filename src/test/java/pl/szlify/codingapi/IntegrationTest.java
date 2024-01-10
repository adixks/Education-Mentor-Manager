package pl.szlify.codingapi;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.model.dto.*;
import pl.szlify.codingapi.repository.LanguageRepository;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

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


    @BeforeEach
    public void setupDatabase() {

        objectMapper.registerModule(new JavaTimeModule());

        LanguageEntity language = new LanguageEntity()
                .setName("Java");
        entityManager.persist(language);

        TeacherEntity teacher = new TeacherEntity()
                .setFirstName("Jan")
                .setLastName("Smith")
                .setLanguages(new HashSet<>());
        teacher.getLanguages().add(language);
        entityManager.persist(teacher);

        StudentEntity student = new StudentEntity()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguage(language)
                .setTeacher(teacher);
        entityManager.persist(student);

        LessonEntity lesson = new LessonEntity()
                .setDate(LocalDateTime.now().plusYears(10L))
                .setTeacher(teacher)
                .setStudent(student);
        entityManager.persist(lesson);

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
        LanguageShortDto languageShortDto = new LanguageShortDto().setName("Python");

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
                .andExpect(jsonPath("$.content[0].firstName").value("Jan"));
    }

    @Test
    public void getTeacher_ReturnsTeacher() throws Exception {
        mockMvc.perform(get("/api/v1/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"));
    }

    @Test
    public void addTeacher_ReturnsNewTeacher() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguages(new HashSet<>() {{
                    add("Java");
                }});

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Adrian"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertEquals(2, teachers.size());
    }

    @Test
    public void updateEntireTeacher_ReturnsUpdatedTeacher() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguages(new HashSet<>() {{
                    add("Java");
                }});

        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Adrian"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertEquals(1, teachers.size());
    }

    @Test
    public void updateTeacherLanguagesList_ReturnsUpdatedTeacher() throws Exception {
        TeacherLanguagesDto teacherLanguagesDto = new TeacherLanguagesDto()
                .setLanguagesList(Arrays.asList("Java", "Python"));

        mockMvc.perform(patch("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherLanguagesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.languages[0]").value("Java"))
                .andExpect(jsonPath("$.languages[1]").value("Python"));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertEquals(1, teachers.size());
    }

    @Test
    public void getStudentsList_ReturnsListOfStudents() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Adrian"));
    }

    @Test
    public void getStudent_ReturnsStudent() throws Exception {
        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Adrian"));
    }

    @Test
    public void addStudent_ReturnsNewStudent() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguages(new HashSet<>() {{
                    add("Java");
                }});

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk());

        StudentShortDto studentShortDto = new StudentShortDto()
                .setFirstName("Alicja")
                .setLastName("Czajka")
                .setLanguage("Java")
                .setTeacherId(2L);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alicja"))
                .andExpect(jsonPath("$.lastName").value("Czajka"));

        List<StudentEntity> students = studentRepository.findAll();
        assertEquals(2, students.size());
    }

    @Test
    public void updateEntireStudent_ReturnsUpdatedStudent() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguages(new HashSet<>() {{
                    add("Java");
                }});

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk());

        StudentShortDto studentShortDto = new StudentShortDto()
                .setFirstName("Adam")
                .setLastName("Kowal")
                .setLanguage("Java")
                .setTeacherId(2L);

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentShortDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Adam"))
                .andExpect(jsonPath("$.lastName").value("Kowal"));

        List<StudentEntity> students = studentRepository.findAll();
        assertEquals(1, students.size());
    }

    @Test
    public void updateStudentTeacher_ReturnsUpdatedStudent() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguages(new HashSet<>() {{
                    add("Java");
                }});

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(2L)))
                .andExpect(status().isOk());

        StudentEntity updatedStudent = studentRepository.findById(1L).orElse(null);
        assertNotNull(updatedStudent);
        assertEquals(2L, updatedStudent.getTeacher().getId());
    }

    @Test
    public void getAllLessons_ReturnsListOfLessons() throws Exception {
        mockMvc.perform(get("/api/v1/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].date").isNotEmpty());
    }

    @Test
    public void getLesson_ReturnsLesson() throws Exception {
        mockMvc.perform(get("/api/v1/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").isNotEmpty());
    }

    @Test
    public void addLesson_ReturnsNewLesson() throws Exception {
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName("Adrian")
                .setLastName("Kowalski")
                .setLanguages(new HashSet<>() {{
                    add("Java");
                }});

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teacherShortDto)))
                .andExpect(status().isOk());

        StudentShortDto studentShortDto = new StudentShortDto()
                .setFirstName("Alicja")
                .setLastName("Czajka")
                .setLanguage("Java")
                .setTeacherId(2L);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentShortDto)))
                .andExpect(status().isOk());

        LessonDto lessonDto = new LessonDto()
                .setDate(LocalDateTime.now().plusYears(9L))
                .setTeacherId(2L)
                .setStudentId(2L);

        String formattedDate = lessonDto.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(formattedDate));

        List<LessonEntity> lessons = lessonRepository.findAll();
        assertEquals(2, lessons.size());
    }

//    @Test
//    public void updateEntireLesson_ReturnsUpdatedLesson() throws Exception {
//    }

//    @Test
//    public void updateLessonDate_ReturnsUpdatedLesson() throws Exception {
//    }

    @Test
    public void deleteLesson_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/lessons/1"))
                .andExpect(status().isNoContent());

        Optional<LessonEntity> lesson = lessonRepository.findById(1L);
        assertTrue(lesson.isEmpty());
    }
}
