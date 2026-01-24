package nl.moreniekmeijer.lessonplatform.repositories;

import nl.moreniekmeijer.lessonplatform.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByOrderByScheduledDateTimeAsc();

    @Query("""
    SELECT l FROM Lesson l 
    WHERE :role MEMBER OF l.allowedRoles 
      AND l.scheduledDateTime > :now 
    ORDER BY l.scheduledDateTime ASC
""")
    List<Lesson> findNextLessonsForRole(@Param("role") String role, @Param("now") LocalDateTime now);
}
