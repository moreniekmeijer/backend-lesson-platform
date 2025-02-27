package nl.moreniekmeijer.lessonplatform.services;

import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
}
