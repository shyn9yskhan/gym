package com.shyn9yskhan.training_orchestration_service.dto;

import com.shyn9yskhan.training_orchestration_service.client.dto.TrainingDto;

import java.util.List;

public record GetTrainerTrainingsListResponse(List<TrainingDto> trainings) {
}
