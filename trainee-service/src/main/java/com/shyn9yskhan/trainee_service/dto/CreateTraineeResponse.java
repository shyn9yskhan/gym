package com.shyn9yskhan.trainee_service.dto;

import java.time.LocalDate;

public record CreateTraineeResponse(String id, LocalDate dateOfBirth, String address) {
}
