package pl.szlify.codingapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.repository.LanguageRepository;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() {
        lessonRepository.deleteAll();
        languageRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    public void testAddLanguage() throws Exception {
        // Given
        LanguageShortDto newLanguageDto = new LanguageShortDto();
        newLanguageDto.setName("Java");

        // When
        ResultActions resultActions = mockMvc.perform(post("http://localhost:8080/api/v1/language")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLanguageDto)));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.id").exists());

        assertNotNull(resultActions.andReturn().getResponse().getContentAsString());
    }


}
