package com.shyn9yskhan.user_service.dto;

import java.io.Serializable;

public class GetUserIdByUsernameServiceResponse implements Serializable {
    private String userId;
    public GetUserIdByUsernameServiceResponse() {}
    public GetUserIdByUsernameServiceResponse(String userId) { this.userId = userId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
