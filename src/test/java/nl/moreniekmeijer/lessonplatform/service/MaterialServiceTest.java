package nl.moreniekmeijer.lessonplatform.service;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    MaterialRepository materialRepository;
    @Mock
    StyleRepository styleRepository;
    @Mock
    FileService fileService;

    @InjectMocks
    MaterialService materialService;

    @Test
    void addMaterial_shouldSaveAndReturnDto() {
        MaterialInputDto input = new MaterialInputDto();
        input.setTitle("My Title");
        input.setStyleId(1L);
        Style style = new Style();
        Material material = new Material();
        Material saved = new Material();
        saved.setTitle("My Title");

        when(styleRepository.findById(1L)).thenReturn(Optional.of(style));
        when(materialRepository.save(any(Material.class))).thenReturn(saved);

        var result = materialService.addMaterial(input);

        assertEquals("My Title", result.getTitle());
    }

    @Test
    void addMaterial_shouldThrowIfStyleNotFound() {
        MaterialInputDto input = new MaterialInputDto();
        input.setStyleId(99L);

        when(styleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> materialService.addMaterial(input));
    }

    @Test
    void addMaterial_shouldThrowOnDuplicateTitle() {
        MaterialInputDto input = new MaterialInputDto();

        when(materialRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(IllegalArgumentException.class, () -> materialService.addMaterial(input));
    }

    @Test
    void addMaterial_shouldWorkWithoutStyleId() {
        MaterialInputDto input = new MaterialInputDto();
        input.setTitle("No Style Title");
        input.setStyleId(null);
        Material material = new Material();
        Material saved = new Material();
        saved.setTitle("No Style Title");

        when(materialRepository.save(any(Material.class))).thenReturn(saved);

        var result = materialService.addMaterial(input);

        assertEquals("No Style Title", result.getTitle());
    }

    @Test
    void getFilteredMaterials_shouldFilterCorrectly() {
        Material material1 = new Material(); material1.setTitle("Djembe ritme 1"); material1.setFileType(FileType.VIDEO); material1.setInstrument("Djembe"); material1.setCategory("Partij");
        Material material2 = new Material(); material2.setTitle("Makru arrangement"); material2.setFileType(FileType.PDF); material2.setInstrument("Diversen"); material2.setCategory("Arrangement");
        when(materialRepository.findAll()).thenReturn(List.of(material1, material2));

        var result = materialService.getFilteredMaterials("ritme", "video", "Djembe", "Partij", null, null);

        assertEquals(1, result.size());
    }

    @Test
    void getFilteredMaterials_shouldFilterByStyleNameAndOrigin() {
        Style style = new Style();
        style.setName("Afro");
        style.setOrigin("Guinea");

        Material m1 = new Material();
        m1.setTitle("Title");
        m1.setStyle(style);

        when(materialRepository.findAll()).thenReturn(List.of(m1));

        var result = materialService.getFilteredMaterials(null, null, null, null, "Afro", "Guinea");

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void getFilteredMaterials_noFilters_returnsAll() {
        Material mat = new Material();
        mat.setTitle("Cool Song");
        when(materialRepository.findAll()).thenReturn(List.of(mat));

        var result = materialService.getFilteredMaterials(null, null, null, null, null, null);

        assertEquals(1, result.size());
    }


    @Test
    void getMaterialById_shouldReturnDto() {
        Material material = new Material(); material.setId(1L); material.setTitle("Test");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        var result = materialService.getMaterialById(1L);

        assertEquals("Test", result.getTitle());
    }

    @Test
    void getMaterialById_shouldThrowIfNotFound() {
        when(materialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> materialService.getMaterialById(1L));
    }

    @Test
    void deleteMaterial_shouldCallDelete() {
        Material material = new Material();
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        materialService.deleteMaterial(1L);

        verify(materialRepository).delete(material);
    }

//    @Test
//    void assignToMaterial_shouldAssignAndReturnDto() {
//        Material material = new Material(); material.setId(1L); material.setCategory("arrangement"); material.setStyle(new Style());
//        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
//        when(materialRepository.existsByCategoryIgnoreCaseAndFileTypeAndStyleId("arrangement", FileType.PDF, null)).thenReturn(false);
//        when(materialRepository.save(any())).thenReturn(material);
//
//        var result = materialService.assignToMaterial("file.pdf", 1L, FileType.PDF);
//
//        assertEquals("file.pdf", result.getFilePath());
//    }

    @Test
    void assignToMaterial_shouldThrowOnDuplicateArrangement() {
        Material material = new Material(); material.setCategory("arrangement"); material.setStyle(new Style());
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(materialRepository.existsByCategoryIgnoreCaseAndFileTypeAndStyleId(any(), any(), any())).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
                materialService.assignToMaterial("file.pdf", 1L, FileType.PDF));
    }

//    @Test
//    void getFileFromMaterial_shouldReturnResource() {
//        Material material = new Material(); material.setFileType(FileType.PDF); material.setFileName("file.pdf");
//        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
//        Resource mockResource = mock(Resource.class);
//        when(fileService.downloadFile("file.pdf")).thenReturn(mockResource);
//
//        var result = materialService.getFileFromMaterial(1L);
//
//        assertEquals(mockResource, result);
//    }
//
//    @Test
//    void getFileFromMaterial_shouldThrowIfTypeIsLink() {
//        Material material = new Material(); material.setFileType(FileType.LINK);
//        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
//
//        assertThrows(IllegalStateException.class, () -> materialService.getFileFromMaterial(1L));
//    }
}
