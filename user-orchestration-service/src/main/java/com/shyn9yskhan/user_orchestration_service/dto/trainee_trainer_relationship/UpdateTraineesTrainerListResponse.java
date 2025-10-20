package com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship;

import com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto;

import java.util.List;

public record UpdateTraineesTrainerListResponse(List<TrainerDto> trainers) {
}
