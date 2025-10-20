package com.shyn9yskhan.trainee_service.dto;

import java.time.LocalDate;

public record CreateTraineeRequest
        (LocalDate dateOfBirth,
         String address,
         String userId) {
}
