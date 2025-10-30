package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.models.User;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import nl.moreniekmeijer.lessonplatform.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final StyleRepository styleRepository;
    private final FileService fileService;
    private final UserRepository userRepository;

    public MaterialService(MaterialRepository materialRepository, StyleRepository styleRepository, FileService fileService, UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.styleRepository = styleRepository;
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    public MaterialResponseDto addMaterial(MaterialInputDto materialInputDto) {
        Style style = null;
        if (materialInputDto.getStyleId() != null) {
            style = styleRepository.findById(materialInputDto.getStyleId())
                    .orElseThrow(() -> new EntityNotFoundException("Style not found with id: " + materialInputDto.getStyleId()));
        }

        Material material = MaterialMapper.toEntity(materialInputDto, style);

        try {
            Material savedMaterial = materialRepository.save(material);
            return MaterialMapper.toResponseDto(savedMaterial);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("The title must be unique. This title already exists.");
        }
    }

    public List<MaterialResponseDto> getFilteredMaterials(
            String search, String fileType, String instrument,
            String category, String styleName, String origin
    ) {
        List<Material> foundMaterials = materialRepository.findAll();

        if (search != null && !search.isEmpty()) {
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        if (fileType != null && !fileType.isEmpty()) {
            FileType fileTypeEnum = FileType.valueOf(fileType.toUpperCase(Locale.ROOT));
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getFileType() == fileTypeEnum)
                    .toList();
        }

        if (instrument != null && !instrument.isEmpty()) {
            foundMaterials = foundMaterials.stream().filter(m -> m.getInstrument().equals(instrument)).toList();
        }

        if (category != null && !category.isEmpty()) {
            foundMaterials = foundMaterials.stream().filter(m -> m.getCategory().equals(category)).toList();
        }

        if (styleName != null && !styleName.isEmpty()) {
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getStyle() != null && m.getStyle().getName().equals(styleName))
                    .toList();
        }

        if (origin != null && !origin.isEmpty()) {
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getStyle() != null && m.getStyle().getOrigin().equals(origin))
                    .toList();
        }

        return foundMaterials.stream()
                .map(MaterialMapper::toResponseDto)
                .toList();
    }

    public MaterialResponseDto getMaterialById(Long id) {
        Material foundMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));
        return MaterialMapper.toResponseDto(foundMaterial);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        Material foundMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));

        for (User user : foundMaterial.getUsers()) {
            user.getBookmarkedMaterials().remove(foundMaterial);
        }

        if (foundMaterial.getFileType() != FileType.LINK && foundMaterial.getFileName() != null) {
            fileService.deleteFile(foundMaterial.getFileName());
        }

        materialRepository.delete(foundMaterial);
    }

    @Transactional
    public MaterialResponseDto assignToMaterial(String fileNameOrLink, Long materialId, FileType fileType) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + materialId));

        material.setFileName(fileNameOrLink);

        if ("arrangement".equalsIgnoreCase(material.getCategory()) && fileType == FileType.PDF) {
            boolean existsArrangement = materialRepository.existsByCategoryIgnoreCaseAndFileTypeAndStyleId(
                    "arrangement", FileType.PDF, material.getStyle() != null ? material.getStyle().getId() : null);

            if (existsArrangement) {
                throw new IllegalStateException("Material with fileType 'PDF' and category 'Arrangement' already exists for this style.");
            }
        }

        material.setFileType(fileType);

        Material updatedMaterial = materialRepository.save(material);

        return MaterialMapper.toResponseDto(updatedMaterial);
    }

    @Transactional
    public String getSignedUrlForMaterial(Long id, String action) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));

        if (material.getFileType() == FileType.LINK) {
            throw new IllegalStateException("Cannot get signed URL for LINK type material.");
        }

        if (material.getFileName() == null) {
            throw new IllegalStateException("No file name stored for this material.");
        }

        boolean download = "download".equalsIgnoreCase(action);
        return fileService.generateSignedUrl(material.getFileName(), download);
    }

    public void ensureExists(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new EntityNotFoundException("Material not found with id: " + id);
        }
    }
}
