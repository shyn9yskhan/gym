package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class DeleteUserByUsernameServiceResponse {
    private String id;

    public DeleteUserByUsernameServiceResponse() {
    }

    public DeleteUserByUsernameServiceResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
