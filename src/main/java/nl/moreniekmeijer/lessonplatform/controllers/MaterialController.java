package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.FileResponseDto;
import nl.moreniekmeijer.lessonplatform.dtos.LinkInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.services.FileService;
import nl.moreniekmeijer.lessonplatform.services.MaterialService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final FileService fileService;

    public MaterialController(MaterialService materialService, FileService fileService) {
        this.materialService = materialService;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDto> addMaterial(@Valid @RequestBody MaterialInputDto materialInputDto) {
        MaterialResponseDto savedMaterial = materialService.addMaterial(materialInputDto);
        URI location = URIUtil.createResourceUri(savedMaterial.getId());
        return ResponseEntity.created(location).body(savedMaterial);
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

    @PostMapping("/{id}/file")
    public ResponseEntity<MaterialResponseDto> addFileToMaterial(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        FileResponseDto fileResponse = fileService.saveFile(file);
        MaterialResponseDto savedMaterial = materialService.assignToMaterial(fileResponse.getObjectName(), id, fileResponse.getFileType());
//        URI location = URIUtil.createFileAssignmentUri(id);
        return ResponseEntity.ok(savedMaterial);
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
    public ResponseEntity<MaterialResponseDto> addLinkToMaterial(@PathVariable Long id, @Valid @RequestBody LinkInputDto linkInputDto) throws IOException {
        String link = linkInputDto.getLink();
        MaterialResponseDto savedMaterial = materialService.assignToMaterial(link, id, FileType.LINK);
//        URI location = URIUtil.createLinkAssignmentUri(id);
        return ResponseEntity.ok(savedMaterial);
    }
}