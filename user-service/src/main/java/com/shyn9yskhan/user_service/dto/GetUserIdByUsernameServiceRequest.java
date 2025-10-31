package com.shyn9yskhan.user_service.dto;

import java.io.Serializable;

public class GetUserIdByUsernameServiceRequest implements Serializable {
    private String username;
    public GetUserIdByUsernameServiceRequest() {}
    public GetUserIdByUsernameServiceRequest(String username) { this.username = username; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
