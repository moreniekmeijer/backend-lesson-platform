package nl.moreniekmeijer.lessonplatform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.moreniekmeijer.lessonplatform.LessonPlatformApplication;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.filter.JwtRequestFilter;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import nl.moreniekmeijer.lessonplatform.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(classes = LessonPlatformApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@ComponentScan(basePackages = "nl.moreniekmeijer.lessonplatform") // Pas aan naar jouw root package
class MaterialControllerIntegrationTest {

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private Long styleId;

    @BeforeEach
    void setUp() {
        lessonRepository.deleteAll();
        materialRepository.deleteAll();
        styleRepository.deleteAll();

        Style style = new Style();
        style.setName("Baiao");
        style.setOrigin("Brazil");
        style = styleRepository.save(style);
        styleId = style.getId();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAddMaterial_andThenGetAllMaterials() throws Exception {
        MaterialInputDto dto = new MaterialInputDto();
        dto.setTitle("Test Material");
        dto.setInstrument("Surdo");
        dto.setCategory("Break");
        dto.setStyleId(styleId);

        mockMvc.perform(post("/materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Material"))
                .andExpect(jsonPath("$.instrument").value("Surdo"))
                .andExpect(jsonPath("$.category").value("Break"))
                .andExpect(jsonPath("$.styleId").value(styleId))
                        .andDo(print());

        mockMvc.perform(get("/materials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Material"))
                .andExpect(jsonPath("$[0].instrument").value("Surdo"))
                .andExpect(jsonPath("$[0].category").value("Break"))
                .andExpect(jsonPath("$[0].styleId").value(styleId));
    }
}
