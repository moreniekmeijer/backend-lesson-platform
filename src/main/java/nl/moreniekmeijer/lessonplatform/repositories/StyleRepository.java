package nl.moreniekmeijer.lessonplatform.repositories;

import nl.moreniekmeijer.lessonplatform.models.Style;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StyleRepository extends JpaRepository<Style, Long> {
    Optional<Style> findByName(String name);
}
