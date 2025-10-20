package com.shyn9yskhan.user_orchestration_service.dto.trainee;

import java.time.LocalDate;

public record TraineeDto(String username, String firstname, String lastname, LocalDate dateOfBirth, String address) {
}
