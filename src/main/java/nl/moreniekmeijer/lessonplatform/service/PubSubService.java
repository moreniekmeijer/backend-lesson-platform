package nl.moreniekmeijer.lessonplatform.service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class PubSubService {

    private final boolean localMode;
    private Publisher publisher;

    public PubSubService(
            @Value("${gcp.project}") String project,
            @Value("${gcp.topic}") String topic,
            @Value("${pubsub.local-mode:true}") boolean localMode
    ) throws IOException {
        this.localMode = localMode;

        if (!localMode) {
            TopicName topicName = TopicName.of(project, topic);
            this.publisher = Publisher.newBuilder(topicName).build();
        } else {
            System.out.println("[PubSubService] Running in LOCAL mode, messages will be logged.");
        }
    }

    public void publishVideoJob(Long materialId, String objectName) {
        String messageJson = """
                {"materialId": %d, "objectName": "%s"}
                """.formatted(materialId, objectName);

        if (localMode) {
            System.out.println("[PubSubService][LOCAL] Publish: " + messageJson);
            return;
        }

        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(messageJson))
                .build();

        try {
            publisher.publish(pubsubMessage).get(); // synchroon publish
            System.out.println("[PubSubService] Published message: " + messageJson);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to publish Pub/Sub message", e);
        }
    }

    @PreDestroy
    public void shutdown() throws Exception {
        if (publisher != null) {
            publisher.shutdown();
        }
    }
}
