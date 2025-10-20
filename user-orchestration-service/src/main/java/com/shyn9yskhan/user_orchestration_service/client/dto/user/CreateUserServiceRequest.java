package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class CreateUserServiceRequest {
    private String firstname;
    private String lastname;

    public CreateUserServiceRequest() {
    }

    public CreateUserServiceRequest(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
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
}
