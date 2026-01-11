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
        System.out.println("[VideoProcessing] Starting for " + objectName);

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
            System.out.println("[VideoProcessing] MOV downloaded: " + tempMov.getAbsolutePath());

            tempMp4 = convertMovToMp4(tempMov);
            System.out.println("[VideoProcessing] Conversion finished: " + tempMp4.getAbsolutePath());

            String mp4ObjectName = objectName.replaceFirst("(?i)\\.mov$", "") + "_compressed.mp4";
            fileService.uploadFile(tempMp4, mp4ObjectName, "video/mp4");
            System.out.println("[VideoProcessing] MP4 uploaded as " + mp4ObjectName);

            materialService.replaceMaterialFile(materialId, mp4ObjectName, FileType.VIDEO);

            fileService.deleteFile(objectName);
            System.out.println("[VideoProcessing] MOV deleted, DB updated");

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
