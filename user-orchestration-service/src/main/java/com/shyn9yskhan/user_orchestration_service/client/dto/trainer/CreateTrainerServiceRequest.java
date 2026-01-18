package com.shyn9yskhan.user_orchestration_service.client.dto.trainer;

public class CreateTrainerServiceRequest {
    private String specialization;
    private String userId;

    public CreateTrainerServiceRequest() {
    }

    public CreateTrainerServiceRequest(String specialization, String userId) {
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
