package nl.moreniekmeijer.lessonplatform.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.filter.JwtRequestFilter;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class LessonControllerIntegrationTest {

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @BeforeEach
    void setup() {
        lessonRepository.deleteAll();
        styleRepository.deleteAll();
    }

    @Test
    void testCreateLesson_withValidStyleIds_returnsCreatedLesson() throws Exception {
        Style style = new Style();
        style.setName("Baiao");
        style = styleRepository.save(style);

        LessonInputDto inputDto = new LessonInputDto();
        inputDto.setScheduledDateTime(LocalDateTime.of(2025, 5, 1, 10, 0));
        inputDto.setNotes("Integratietest les");
        inputDto.setStyleIds(List.of(style.getId()));

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.scheduledDateTime").value("2025-05-01T10:00:00"))
                .andExpect(jsonPath("$.notes").value("Integratietest les"))
                .andExpect(jsonPath("$.styleIds[0]").value(style.getId()));
    }

    @Test
    void testCreateLesson_withoutStyles_returnsBadRequest() throws Exception {
        LessonInputDto inputDto = new LessonInputDto();
        inputDto.setScheduledDateTime(LocalDateTime.of(2025, 5, 1, 10, 0));
        inputDto.setNotes("Ongeldige les");

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest());
    }
}
