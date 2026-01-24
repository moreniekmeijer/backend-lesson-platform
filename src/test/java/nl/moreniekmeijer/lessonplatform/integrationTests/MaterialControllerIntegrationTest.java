package nl.moreniekmeijer.lessonplatform.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.filter.JwtRequestFilter;
import nl.moreniekmeijer.lessonplatform.models.Material;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MaterialControllerIntegrationTest {

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
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
    private String styleName;

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
        styleName = style.getName();
    }

    @Test
    void testAddMaterial_returnsCreatedMaterial() throws Exception {
        MaterialInputDto dto = new MaterialInputDto();
        dto.setTitle("Test Material");
//        dto.setInstruments("Surdo");
        dto.setCategory("Break");
        dto.setStyleId(styleId);

        mockMvc.perform(post("/materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Material"))
//                .andExpect(jsonPath("$.instrument").value("Surdo"))
                .andExpect(jsonPath("$.category").value("Break"))
                .andExpect(jsonPath("$.styleName").value("Baiao"))
                .andDo(print());
    }

    @Test
    void testGetAllMaterials_returnsListWithExpectedMaterial() throws Exception {
        Material material = new Material();
        material.setTitle("Test Material");
//        material.setInstrument("Surdo");
        material.setCategory("Break");
        material.setStyle(styleRepository.findById(styleId).orElse(null));
        materialRepository.save(material);

        mockMvc.perform(get("/materials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Material"))
//                .andExpect(jsonPath("$[0].instrument").value("Surdo"))
                .andExpect(jsonPath("$[0].category").value("Break"))
                .andExpect(jsonPath("$[0].styleName").value(styleName));
    }
}
