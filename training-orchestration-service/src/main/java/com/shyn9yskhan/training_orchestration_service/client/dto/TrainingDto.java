package com.shyn9yskhan.training_orchestration_service.client.dto;

import java.time.LocalDate;

public record TrainingDto(
        String traineeId,
        String trainerId,
        String trainingName,
        String trainingTypeId,
        LocalDate date,
        int duration
) {}
