package nl.moreniekmeijer.lessonplatform.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class VideoProcessingService {

    private final FileService fileService;
    private final MaterialService materialService;

    public VideoProcessingService(FileService fileService, MaterialService materialService) {
        this.fileService = fileService;
        this.materialService = materialService;
    }

    public void processBlocking(Long materialId, String objectName) {
        File tempMov = null;
        File tempMp4 = null;

        try {
            tempMov = File.createTempFile("mov_", ".mov");

            Blob blob = fileService.getStorage().get(BlobId.of(fileService.getBucketName(), objectName));
            if (blob == null) {
                System.err.println("[VideoProcessing] MOV not found in bucket: " + objectName);
                return;
            }
            blob.downloadTo(tempMov.toPath());

            tempMp4 = convertMovToMp4(tempMov);

            String mp4ObjectName = objectName.replaceFirst("(?i)\\.mov$", "") + "_compressed.mp4";
            fileService.uploadFile(tempMp4, mp4ObjectName, "video/mp4");

            materialService.replaceMaterialFile(materialId, mp4ObjectName, FileType.VIDEO);

            fileService.deleteFile(objectName);

        } catch (Exception e) {
            System.err.println("[VideoProcessing] Failed for " + objectName);
            e.printStackTrace();
        } finally {
            if (tempMov != null && tempMov.exists()) tempMov.delete();
            if (tempMp4 != null && tempMp4.exists()) tempMp4.delete();
        }
    }

    private File convertMovToMp4(File movFile) throws IOException, InterruptedException {
        File outputFile = File.createTempFile("converted_", ".mp4");

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-i", movFile.getAbsolutePath(),
                "-vcodec", "libx264",
                "-preset", "ultrafast",
                "-crf", "28",
                "-vf", "scale=720:-2",
                "-pix_fmt", "yuv420p",
                "-movflags", "+faststart",
                "-c:a", "aac",
                "-ar", "44100",
                "-ac", "2",
                "-b:a", "128k",
                "-loglevel", "error",
                outputFile.getAbsolutePath()
        );

        int exitCode = pb.start().waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("ffmpeg failed with exit code " + exitCode);
        }

        return outputFile;
    }
}
