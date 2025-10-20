package com.shyn9yskhan.training_orchestration_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record GetTrainerTrainingsListRequest(
        @NotBlank(message = "username is required") String username,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate periodFrom,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate periodTo,
        String traineeId,
        String trainingTypeId
) {}
