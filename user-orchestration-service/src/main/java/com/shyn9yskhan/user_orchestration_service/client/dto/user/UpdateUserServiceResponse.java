package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class UpdateUserServiceResponse {
    private String username;
    private String firstname;
    private String lastname;
    private boolean isActive;

    public UpdateUserServiceResponse() {
    }

    public UpdateUserServiceResponse(String username, String firstname, String lastname, boolean isActive) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
