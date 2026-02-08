package nl.moreniekmeijer.lessonplatform.repositories;

import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
//    boolean existsByCategoryIgnoreCaseAndFileTypeAndStyleId(String category, FileType fileType, Long styleId);
}
