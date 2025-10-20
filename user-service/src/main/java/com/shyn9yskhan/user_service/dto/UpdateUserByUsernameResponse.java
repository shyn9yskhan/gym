package com.shyn9yskhan.user_service.dto;

public record UpdateUserByUsernameResponse(String id, String username, String firstname, String lastname, boolean isActive) {}
