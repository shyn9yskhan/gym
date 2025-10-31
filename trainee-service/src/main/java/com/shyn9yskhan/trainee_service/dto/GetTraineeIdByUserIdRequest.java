package com.shyn9yskhan.trainee_service.dto;

public class GetTraineeIdByUserIdRequest {
    private String userId;

    public GetTraineeIdByUserIdRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
