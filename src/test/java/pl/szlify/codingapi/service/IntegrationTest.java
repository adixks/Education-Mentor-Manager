package pl.szlify.codingapi.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.codingapi.model.LanguageEntity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    public void cleanupDatabase() {
        entityManager.createQuery("DELETE FROM LanguageEntity").executeUpdate();
    }

    @Test
    public void getAllLanguage_ReturnsListOfLanguages() throws Exception {
        LanguageEntity language = new LanguageEntity();
        language.setName("Java");
        entityManager.persist(language);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Java"));
    }


}
