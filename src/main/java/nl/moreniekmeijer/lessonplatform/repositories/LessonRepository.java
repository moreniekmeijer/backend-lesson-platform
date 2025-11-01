package nl.moreniekmeijer.lessonplatform.repositories;

import nl.moreniekmeijer.lessonplatform.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByOrderByScheduledDateTimeAsc();

    List<Lesson> findAllByScheduledDateTimeAfterOrderByScheduledDateTimeAsc(LocalDateTime now);
}
