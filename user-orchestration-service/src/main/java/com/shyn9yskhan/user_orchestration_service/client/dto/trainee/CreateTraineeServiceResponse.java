package com.shyn9yskhan.user_orchestration_service.client.dto.trainee;

import java.time.LocalDate;

public class CreateTraineeServiceResponse {
    private String id;
    private LocalDate dateOfBirth;
    private String address;

    public CreateTraineeServiceResponse() {
    }

    public CreateTraineeServiceResponse(String id, LocalDate dateOfBirth, String address) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
