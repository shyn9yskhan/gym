package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class UpdateUserByUsernameServiceResponse {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private boolean isActive;

    public UpdateUserByUsernameServiceResponse() {
    }

    public UpdateUserByUsernameServiceResponse(String id, String username, String firstname, String lastname, boolean isActive) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
