package nl.moreniekmeijer.lessonplatform.service;

import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

@Service
public class CloudTasksService {

    private final String projectId = "bright-petal-476314-d4";
    private final String location = "europe-west1";
    private final String queue = "video-conversion-queue";
    private final String targetUrl = "https://video-converter-service-2-250364193521.europe-west1.run.app/convert";
    private final String serviceAccountEmail = "service-250364193521@gcp-sa-cloudtasks.iam.gserviceaccount.com";

    public void enqueueVideoConversion(Long materialId, String objectName) {
        System.out.println("Creating task for materialId=" + materialId + ", objectName=" + objectName);

        try (CloudTasksClient client = CloudTasksClient.create()) {
            // Maak de HTTP-request en voeg OIDC-token toe
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .setHttpMethod(HttpMethod.POST)
                    .setUrl(targetUrl)
                    .setBody(ByteString.copyFromUtf8(
                            String.format("{\"materialId\":%d,\"objectName\":\"%s\"}", materialId, objectName)
                    ))
                    .putHeaders("Content-Type", "application/json")
                    .setOidcToken(OidcToken.newBuilder()
                            .setServiceAccountEmail(serviceAccountEmail)
                            .setAudience(targetUrl)
                            .build())
                    .build();

            Task task = Task.newBuilder()
                    .setHttpRequest(httpRequest)
                    .build();

            QueueName parent = QueueName.of(projectId, location, queue);
            client.createTask(parent, task);

            System.out.println("Task created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
