package com.shyn9yskhan.user_orchestration_service.client.dto.user;

public class CreateUserServiceRequest {
    private String firstname;
    private String lastname;
    private Role role;

    public CreateUserServiceRequest() {
    }

    public CreateUserServiceRequest(String firstname, String lastname, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
