package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class UpdateUserServiceRequest {
    private String firstname;
    private String lastname;
    private boolean isActive;

    public UpdateUserServiceRequest() {
    }

    public UpdateUserServiceRequest(String firstname, String lastname, boolean isActive) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
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
