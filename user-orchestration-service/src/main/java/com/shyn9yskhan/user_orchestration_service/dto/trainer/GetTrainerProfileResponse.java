package com.shyn9yskhan.user_orchestration_service.dto.trainer;

import com.shyn9yskhan.user_orchestration_service.dto.trainee.TraineeDto;

import java.util.List;

public record GetTrainerProfileResponse(String firstname,
                                        String lastname,
                                        String trainingTypeId,
                                        boolean isActive,
                                        List<TraineeDto> trainees) {
}
