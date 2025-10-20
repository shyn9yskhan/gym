package com.shyn9yskhan.user_orchestration_service.dto.trainee;

import java.time.LocalDate;

public record CreateTraineeRequest(String firstname,
                                   String lastname,
                                   LocalDate dateOfBirth,
                                   String address) {
}
