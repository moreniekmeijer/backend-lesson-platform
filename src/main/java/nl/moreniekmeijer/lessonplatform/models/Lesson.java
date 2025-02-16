package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate scheduledDate;

    public Lesson() {
    }

    public Lesson(Long id, LocalDate scheduledDate) {
        this.id = id;
        this.scheduledDate = scheduledDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
