package com.shyn9yskhan.training_orchestration_service.dto;

import java.io.Serializable;
import java.util.Objects;

public class GetUserIdByUsernameServiceRequest implements Serializable {
    private String username;

    public GetUserIdByUsernameServiceRequest() {}

    public GetUserIdByUsernameServiceRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetUserIdByUsernameServiceRequest)) return false;
        GetUserIdByUsernameServiceRequest that = (GetUserIdByUsernameServiceRequest) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "GetUserIdByUsernameServiceRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
