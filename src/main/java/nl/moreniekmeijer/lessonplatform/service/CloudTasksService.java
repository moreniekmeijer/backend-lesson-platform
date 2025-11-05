package nl.moreniekmeijer.lessonplatform.service;

import com.google.cloud.tasks.v2.CloudTasksClient;
import com.google.cloud.tasks.v2.HttpMethod;
import com.google.cloud.tasks.v2.HttpRequest;
import com.google.cloud.tasks.v2.QueueName;
import com.google.cloud.tasks.v2.Task;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

@Service
public class CloudTasksService {

    private final String projectId = "PROJECT_ID";
    private final String location = "europe-west4";
    private final String queue = "video-conversion-queue";
    private final String targetUrl = "https://video-converter-service-xyz.a.run.app/convert";

    public void enqueueVideoConversion(Long materialId, String objectName) {
        try (CloudTasksClient client = CloudTasksClient.create()) {
            Task.Builder taskBuilder = Task.newBuilder()
                    .setHttpRequest(HttpRequest.newBuilder()
                            .setHttpMethod(HttpMethod.POST)
                            .setUrl(targetUrl)
                            .setBody(ByteString.copyFromUtf8(
                                    String.format("{\"materialId\":%d,\"objectName\":\"%s\"}", materialId, objectName)
                            ))
                            .putHeaders("Content-Type", "application/json")
                    );

            QueueName parent = QueueName.of(projectId, location, queue);
            client.createTask(parent, taskBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
