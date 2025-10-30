package com.shyn9yskhan.authentication_service.dto;

public class GenerateTokenResponse {
    private String token;

    public GenerateTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
