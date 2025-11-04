package nl.moreniekmeijer.lessonplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "nl.moreniekmeijer.lessonplatform")
@EnableAsync
public class LessonPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(LessonPlatformApplication.class, args);
    }
}
