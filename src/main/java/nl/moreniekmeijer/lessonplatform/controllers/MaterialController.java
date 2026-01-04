package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.LinkInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.service.FileService;
import nl.moreniekmeijer.lessonplatform.service.MaterialService;
import nl.moreniekmeijer.lessonplatform.service.PubSubService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final FileService fileService;
    private final PubSubService pubSubService;

    public MaterialController(MaterialService materialService, FileService fileService, PubSubService pubSubService) {
        this.materialService = materialService;
        this.fileService = fileService;
        this.pubSubService = pubSubService;
    }

    /**
     * Create a material (metadata) and optionally return a signed upload URL.
     * Frontend can upload the file directly to GCS using the returned uploadUrl.
     */
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
    ) throws Exception {
        String objectName = payload.get("objectName");
        FileType fileType = FileType.valueOf(payload.get("fileType"));

        MaterialResponseDto material =
                materialService.assignToMaterial(objectName, id, fileType);

        if (fileType == FileType.VIDEO && objectName.toLowerCase().endsWith(".mov")) {
            pubSubService.publishVideoJob(material.getId(), objectName);
        }

        return ResponseEntity.ok(material);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Void> getFile(
            @PathVariable Long id,
            @RequestParam(defaultValue = "view") String action
    ) {
        String signedUrl = materialService.getSignedUrlForMaterial(id, action);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, signedUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }

    @PostMapping("/{id}/link")
    public ResponseEntity<MaterialResponseDto> addLinkToMaterial(
            @PathVariable Long id,
            @Valid @RequestBody LinkInputDto linkInputDto
    ) throws IOException {
        String link = linkInputDto.getLink();
        MaterialResponseDto savedMaterial = materialService.assignToMaterial(link, id, FileType.LINK);
        return ResponseEntity.ok(savedMaterial);
    }
}
