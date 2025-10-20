package com.shyn9yskhan.trainee_service.dto;

import java.time.LocalDate;

public record UpdateTraineeByUserIdResponse(String traineeId, LocalDate dateOfBirth, String address) {}
