package com.shyn9yskhan.trainee_service.dto;

import java.time.LocalDate;

public record UpdateTraineeRequest(LocalDate dateOfBirth, String address) {
}
