package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.LinkInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.service.CloudTasksService;
import nl.moreniekmeijer.lessonplatform.service.FileService;
import nl.moreniekmeijer.lessonplatform.service.MaterialService;
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
    private final CloudTasksService cloudTasksService;

    public MaterialController(MaterialService materialService, FileService fileService, CloudTasksService cloudTasksService) {
        this.materialService = materialService;
        this.fileService = fileService;
        this.cloudTasksService = cloudTasksService;
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
        // 1. Create the material record in DB
        MaterialResponseDto savedMaterial = materialService.addMaterial(materialInputDto);

        // Prepare base response
        Map<String, Object> response = new HashMap<>();
        response.put("material", savedMaterial);

        // 2. If a file is provided, generate signed upload URL
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

    /**
     * Called after the frontend has finished uploading the file to GCS directly.
     * Associates the uploaded file with the material record.
     */
    @PostMapping("/{id}/confirm-upload")
    public ResponseEntity<MaterialResponseDto> confirmUpload(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String objectName = payload.get("objectName");
        FileType fileType = FileType.valueOf(payload.get("fileType"));

        System.out.println("[confirmUpload] objectName=" + objectName + ", fileType=" + fileType);

        // 1. Koppel bestand aan material
        MaterialResponseDto material = materialService.assignToMaterial(objectName, id, fileType);
        System.out.println("[confirmUpload] Material gekoppeld: " + material);

        // 2. Async MOV → MP4 conversie (callback update DB)
        if (fileType == FileType.VIDEO) {
            // Trim whitespace en vergelijk case-insensitive
            String trimmedName = objectName.trim();
            if (trimmedName.toLowerCase().endsWith(".mov")) {
                System.out.println("[DEBUG] Enqueueing Cloud Task for MOV file: " + trimmedName);

                try {
                    cloudTasksService.enqueueVideoConversion(id, trimmedName);
                    System.out.println("[DEBUG] Cloud Task successfully created for materialId=" + id);
                } catch (Exception e) {
                    System.err.println("[ERROR] Failed to enqueue Cloud Task: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("[DEBUG] File is not a MOV, skipping Cloud Task: " + trimmedName);
            }
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
