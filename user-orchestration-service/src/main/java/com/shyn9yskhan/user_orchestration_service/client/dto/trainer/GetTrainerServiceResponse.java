package com.shyn9yskhan.user_orchestration_service.client.dto.trainer;

public class GetTrainerServiceResponse {
    private String specialization;

    public GetTrainerServiceResponse() {
    }

    public GetTrainerServiceResponse(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
