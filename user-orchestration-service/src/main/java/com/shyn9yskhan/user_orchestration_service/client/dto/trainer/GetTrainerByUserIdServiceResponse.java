package com.shyn9yskhan.user_orchestration_service.client.dto.trainer;

public class GetTrainerByUserIdServiceResponse {
    private String id;
    private String specialization;
    private String userId;

    public GetTrainerByUserIdServiceResponse() {
    }

    public GetTrainerByUserIdServiceResponse(String id, String specialization, String userId) {
        this.id = id;
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
