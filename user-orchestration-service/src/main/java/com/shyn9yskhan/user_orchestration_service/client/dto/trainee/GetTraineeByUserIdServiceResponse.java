package com.shyn9yskhan.user_orchestration_service.client.dto.trainee;

import java.time.LocalDate;

public class GetTraineeByUserIdServiceResponse {
    private String id;
    private LocalDate dateOfBirth;
    private String address;
    private String userId;

    public GetTraineeByUserIdServiceResponse() {
    }

    public GetTraineeByUserIdServiceResponse(String id, LocalDate dateOfBirth, String address, String userId) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
