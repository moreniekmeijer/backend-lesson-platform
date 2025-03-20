package nl.moreniekmeijer.lessonplatform.repositories;

import nl.moreniekmeijer.lessonplatform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}