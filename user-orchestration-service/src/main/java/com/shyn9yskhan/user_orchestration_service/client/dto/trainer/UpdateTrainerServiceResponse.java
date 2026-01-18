package com.shyn9yskhan.user_orchestration_service.client.dto.trainer;

public class UpdateTrainerServiceResponse {
    private String specialization;

    public UpdateTrainerServiceResponse() {
    }

    public UpdateTrainerServiceResponse(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
