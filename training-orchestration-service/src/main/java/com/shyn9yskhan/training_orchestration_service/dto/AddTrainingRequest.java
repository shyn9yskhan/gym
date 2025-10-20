package com.shyn9yskhan.training_orchestration_service.dto;

import java.time.LocalDate;

public record AddTrainingRequest(
        String traineeUsername,
        String trainerUsername,
        String trainingName,
        LocalDate date,
        int duration
) {}
