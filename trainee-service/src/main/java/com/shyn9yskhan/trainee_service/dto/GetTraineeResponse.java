package com.shyn9yskhan.trainee_service.dto;

import java.time.LocalDate;

public record GetTraineeResponse(LocalDate dateOfBirth, String address) {
}
