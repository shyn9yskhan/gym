package com.shyn9yskhan.user_orchestration_service.dto.trainee;

import com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto;

import java.time.LocalDate;
import java.util.List;

public record UpdateTraineeProfileResponse(String username,
                                           String firstname,
                                           String lastname,
                                           LocalDate dateOfBirth,
                                           String address,
                                           boolean isActive,
                                           List<TrainerDto> trainers) {
}
