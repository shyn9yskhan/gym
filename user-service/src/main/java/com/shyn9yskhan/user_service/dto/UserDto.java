package com.shyn9yskhan.user_service.dto;

public record UserDto(
        String id,
        String firstname,
        String lastname,
        String username,
        String password,
        boolean isActive
) {}
