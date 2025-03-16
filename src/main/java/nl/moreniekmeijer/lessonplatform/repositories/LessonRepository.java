package nl.moreniekmeijer.lessonplatform.repositories;

import nl.moreniekmeijer.lessonplatform.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findFirstByScheduledDateTimeAfterOrderByScheduledDateTimeAsc(LocalDateTime now);
}
