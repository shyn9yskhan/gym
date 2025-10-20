package com.shyn9yskhan.user_service.dto;

public record ChangePasswordRequest(String oldPassword, String newPassword) {}
