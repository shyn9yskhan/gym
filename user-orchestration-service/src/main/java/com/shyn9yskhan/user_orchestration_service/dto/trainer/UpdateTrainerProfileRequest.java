package com.shyn9yskhan.user_orchestration_service.dto.trainer;

public record UpdateTrainerProfileRequest(String username,
                                          String firstname,
                                          String lastname,
                                          String trainingTypeId,
                                          boolean isActive) {
}
