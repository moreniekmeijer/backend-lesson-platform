package nl.moreniekmeijer.lessonplatform.dtos;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

public record ConversionRequest(Long materialId, String objectName) {}
