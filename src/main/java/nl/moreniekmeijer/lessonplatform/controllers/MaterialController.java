package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.LinkInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.service.FileService;
import nl.moreniekmeijer.lessonplatform.service.MaterialService;
import nl.moreniekmeijer.lessonplatform.service.VideoProcessingService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final FileService fileService;
    private final VideoProcessingService videoProcessingService;

    public MaterialController(MaterialService materialService, FileService fileService, VideoProcessingService videoProcessingService) {
        this.materialService = materialService;
        this.fileService = fileService;
        this.videoProcessingService = videoProcessingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addMaterialWithUpload(
            @Valid @RequestBody MaterialInputDto materialInputDto,
            @RequestParam(required = false) String filename,
            @RequestParam(required = false) String contentType
    ) {
        MaterialResponseDto savedMaterial = materialService.addMaterial(materialInputDto);

        Map<String, Object> response = new HashMap<>();
        response.put("material", savedMaterial);

        if (filename != null && contentType != null) {
            FileType fileType = fileService.getFileTypeFromFilename(filename);

            String objectName = UUID.randomUUID() + "_" + filename;
            String uploadUrl = fileService.generateSignedUploadUrl(objectName, contentType);

            response.put("uploadUrl", uploadUrl);
            response.put("objectName", objectName);
            response.put("fileType", fileType.name());
        }

        URI location = URIUtil.createResourceUri(savedMaterial.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDto>> getFilteredMaterials(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String styleName,
            @RequestParam(required = false) String origin
    ) {
        List<MaterialResponseDto> filteredMaterials = materialService.getFilteredMaterials(
                search, fileType, instrument, category, styleName, origin
        );
        return ResponseEntity.ok(filteredMaterials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDto> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/confirm-upload")
    public ResponseEntity<MaterialResponseDto> confirmUpload(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String objectName = payload.get("objectName");
        FileType fileType = FileType.valueOf(payload.get("fileType"));

        MaterialResponseDto material =
                materialService.assignToMaterial(objectName, id, fileType);

        if (fileType == FileType.VIDEO && objectName.toLowerCase().endsWith(".mov")) {
            videoProcessingService.processBlocking(material.getId(), objectName);
        }

        return ResponseEntity.ok(material);
    }

    @PostMapping("/{id}/link")
    public ResponseEntity<MaterialResponseDto> addLinkToMaterial(
            @PathVariable Long id,
            @Valid @RequestBody LinkInputDto linkInputDto
    ) {
        String link = linkInputDto.getLink();
        MaterialResponseDto savedMaterial = materialService.assignToMaterial(link, id, FileType.LINK);
        return ResponseEntity.ok(savedMaterial);
    }
}
