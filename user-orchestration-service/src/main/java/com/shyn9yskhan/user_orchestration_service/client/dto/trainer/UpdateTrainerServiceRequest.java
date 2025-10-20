package com.shyn9yskhan.user_orchestration_service.client.dto.trainer;

public class UpdateTrainerServiceRequest {
    private String specialization;

    public UpdateTrainerServiceRequest() {
    }

    public UpdateTrainerServiceRequest(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
