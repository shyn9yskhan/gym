package com.shyn9yskhan.user_orchestration_service.client.dto.trainer;

public class CreateTrainerServiceRequest {
    private String specialization;

    public CreateTrainerServiceRequest() {
    }

    public CreateTrainerServiceRequest(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
