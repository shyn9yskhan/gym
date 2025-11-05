package com.shyn9yskhan.user_service.dto;

import com.shyn9yskhan.user_service.entity.Role;

public record CreateUserRequest(String firstname, String lastname, Role role) {
}
