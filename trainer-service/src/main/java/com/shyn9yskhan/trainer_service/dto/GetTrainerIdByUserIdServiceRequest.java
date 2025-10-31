package com.shyn9yskhan.trainer_service.dto;

import java.io.Serializable;

public class GetTrainerIdByUserIdServiceRequest implements Serializable {
    private String userId;
    public GetTrainerIdByUserIdServiceRequest() {}
    public GetTrainerIdByUserIdServiceRequest(String userId) { this.userId = userId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
