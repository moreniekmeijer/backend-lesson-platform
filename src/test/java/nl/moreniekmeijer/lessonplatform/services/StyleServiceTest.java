package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StyleServiceTest {

    @Mock
    StyleRepository styleRepository;
    @Mock
    MaterialRepository materialRepository;

    @InjectMocks
    StyleService styleService;

    @Test
    void addStyle_shouldReturnResponseDto() {
        StyleInputDto input = new StyleInputDto();
        input.setName("Baiao");

        Style style = new Style(); style.setName("Baiao");
        when(styleRepository.save(any())).thenReturn(style);

        var result = styleService.addStyle(input);

        assertEquals("Baiao", result.getName());
    }

    @Test
    void addStyle_shouldThrowIfNameNotUnique() {
        when(styleRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(IllegalArgumentException.class, () -> styleService.addStyle(new StyleInputDto()));
    }

    @Test
    void getAllStyles_shouldReturnAll() {
        Style s1 = new Style(); s1.setName("Baiao");
        when(styleRepository.findAll()).thenReturn(List.of(s1));

        var result = styleService.getAllStyles();

        assertEquals(1, result.size());
    }

    @Test
    void getStyleById_shouldReturnStyle() {
        Style style = new Style(); style.setName("Zoku");
        when(styleRepository.findById(1L)).thenReturn(Optional.of(style));

        var result = styleService.getStyleById(1L);

        assertEquals("Zoku", result.getName());
    }

    @Test
    void getStyleById_shouldThrowIfNotFound() {
        when(styleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> styleService.getStyleById(99L));
    }

    @Test
    void deleteStyle_shouldDeleteStyle() {
        Style style = new Style();
        style.setLessons(List.of());
        when(styleRepository.findById(1L)).thenReturn(Optional.of(style));

        styleService.deleteStyle(1L);

        verify(styleRepository).delete(style);
    }

    @Test
    void deleteStyle_removesStyleFromLessonsAndDeletesStyle() {
        // Arrange
        Style style = new Style();
        style.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setStyles(new HashSet<>(Set.of(style)));

        style.setLessons(List.of(lesson));

        when(styleRepository.findById(1L)).thenReturn(Optional.of(style));

        styleService.deleteStyle(1L);

        assertFalse(lesson.getStyles().contains(style));
        verify(styleRepository).delete(style);
    }


    @Test
    void assignMaterialToStyle_shouldAssign() {
        Style style = new Style(); style.setId(1L); style.setMaterials(new java.util.ArrayList<>());
        Material material = new Material(); material.setId(2L);

        when(styleRepository.findById(1L)).thenReturn(Optional.of(style));
        when(materialRepository.findById(2L)).thenReturn(Optional.of(material));

        var result = styleService.assignMaterialToStyle(1L, 2L);

        assertEquals(1L, result.getId());
        assertEquals(style, material.getStyle());
    }

    @Test
    void assignMaterialToStyle_shouldReplaceOldStyle() {
        Style oldStyle = new Style(); oldStyle.setId(10L);
        Material material = new Material(); material.setId(2L); material.setStyle(oldStyle);
        oldStyle.setMaterials(new java.util.ArrayList<>(List.of(material)));

        Style newStyle = new Style(); newStyle.setId(1L); newStyle.setMaterials(new java.util.ArrayList<>());

        when(styleRepository.findById(1L)).thenReturn(Optional.of(newStyle));
        when(materialRepository.findById(2L)).thenReturn(Optional.of(material));

        var result = styleService.assignMaterialToStyle(1L, 2L);

        assertEquals(newStyle, material.getStyle());
    }
}
