package com.shyn9yskhan.user_service.dto;

import com.shyn9yskhan.user_service.entity.Role;

public record UserDto(
        String id,
        String firstname,
        String lastname,
        String username,
        String password,
        boolean isActive,
        Role role
) {}
