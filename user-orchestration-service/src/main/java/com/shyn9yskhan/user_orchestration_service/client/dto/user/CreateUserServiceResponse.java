package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class CreateUserServiceResponse {
    private String userId;
    private String username;
    private String password;

    public CreateUserServiceResponse() {
    }

    public CreateUserServiceResponse(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
