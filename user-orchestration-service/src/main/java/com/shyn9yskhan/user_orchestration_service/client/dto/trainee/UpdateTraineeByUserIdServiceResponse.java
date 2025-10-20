package com.shyn9yskhan.user_orchestration_service.client.dto.trainee;

import java.time.LocalDate;

public class UpdateTraineeByUserIdServiceResponse {
    private String traineeId;
    private LocalDate dateOfBirth;
    private String address;

    public UpdateTraineeByUserIdServiceResponse() {
    }

    public UpdateTraineeByUserIdServiceResponse(String traineeId, LocalDate dateOfBirth, String address) {
        this.traineeId = traineeId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
