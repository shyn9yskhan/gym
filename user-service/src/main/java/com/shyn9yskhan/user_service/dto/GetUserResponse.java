package com.shyn9yskhan.user_service.dto;

public record GetUserResponse(
        String firstname,
        String lastname,
        String username,
        String password,
        boolean isActive
) {}
