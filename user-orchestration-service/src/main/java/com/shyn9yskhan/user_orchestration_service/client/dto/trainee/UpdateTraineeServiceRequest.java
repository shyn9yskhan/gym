package com.shyn9yskhan.user_orchestration_service.client.dto.trainee;

import java.time.LocalDate;

public class UpdateTraineeServiceRequest {
    private LocalDate dateOfBirth;
    private String address;

    public UpdateTraineeServiceRequest() {
    }

    public UpdateTraineeServiceRequest(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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
