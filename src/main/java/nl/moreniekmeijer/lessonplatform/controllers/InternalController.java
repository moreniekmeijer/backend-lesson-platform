package nl.moreniekmeijer.lessonplatform.controllers;

import nl.moreniekmeijer.lessonplatform.service.VideoProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalController {

    private final VideoProcessingService videoProcessingService;

    public InternalController(VideoProcessingService videoProcessingService) {
        this.videoProcessingService = videoProcessingService;
    }

    @PostMapping("/process-video")
    public ResponseEntity<String> processVideo(@RequestBody Map<String, Object> payload) {
        try {
            Long materialId = Long.valueOf(payload.get("materialId").toString());
            String objectName = payload.get("objectName").toString();

            videoProcessingService.processSync(materialId, objectName);

            return ResponseEntity.ok("Processed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed");
        }
    }
}
