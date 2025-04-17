package nl.moreniekmeijer.lessonplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "nl.moreniekmeijer.lessonplatform")
public class LessonPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(LessonPlatformApplication.class, args);
    }
}
