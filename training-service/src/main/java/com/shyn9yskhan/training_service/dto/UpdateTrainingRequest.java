package com.shyn9yskhan.training_service.dto;

import java.time.LocalDate;

public record UpdateTrainingRequest(
        String traineeId,
        String trainerId,
        String trainingName,
        String trainingTypeId,
        LocalDate date,
        int duration
) {}
