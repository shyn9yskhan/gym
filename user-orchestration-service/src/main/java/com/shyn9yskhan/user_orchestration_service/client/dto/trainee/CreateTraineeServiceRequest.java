package com.shyn9yskhan.user_orchestration_service.client.dto.trainee;

import java.time.LocalDate;

public class CreateTraineeServiceRequest {
    private LocalDate dateOfBirth;
    private String address;
    private String userId;

    public CreateTraineeServiceRequest() {
    }

    public CreateTraineeServiceRequest(LocalDate dateOfBirth, String address, String userId) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
