package nl.moreniekmeijer.lessonplatform.controllers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import nl.moreniekmeijer.lessonplatform.dtos.ConversionRequest;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.service.FileService;
import nl.moreniekmeijer.lessonplatform.service.MaterialService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/convert")
public class VideoConverterController {

    private final FileService fileService;
    private final MaterialService materialService;

    public VideoConverterController(FileService fileService, MaterialService materialService) {
        this.fileService = fileService;
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<Void> convertVideo(@RequestBody ConversionRequest request) {
        String objectName = request.objectName();
        Long materialId = request.materialId();

        System.out.println("[CloudTasks Controller] New conversion request received for " + objectName);

        try {
            // 1. Download MOV van GCS naar temp file
            File tempMov = File.createTempFile("mov_download_", ".mov");
            Blob blob = fileService.getStorage().get(BlobId.of(fileService.getBucketName(), objectName));
            if (blob == null) {
                return ResponseEntity.notFound().build();
            }
            blob.downloadTo(tempMov.toPath());

            // 2. Converteer naar MP4
            File mp4File = fileService.convertMovToMp4(tempMov);

            // 3. Upload MP4 terug naar GCS
            String newObjectName = objectName.replace(".mov", ".mp4");
            fileService.uploadFile(mp4File, newObjectName, "video/mp4");

            // 4. Update database
            materialService.replaceMaterialFile(materialId, newObjectName, FileType.VIDEO);

            // 5. Cleanup temp files
            tempMov.delete();
            mp4File.delete();

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
