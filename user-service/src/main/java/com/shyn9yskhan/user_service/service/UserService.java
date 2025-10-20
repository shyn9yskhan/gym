package com.shyn9yskhan.user_service.service;

import com.shyn9yskhan.user_service.dto.*;

import java.util.List;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
    GetUserResponse getUser(String userId);
    GetUserByUsernameResponse getUserByUsername(String username);
    GetUserIdByUsernameResponse getUserIdByUsername(String username);
    List<UserDto> getUsersByIds(List<String> userIds);
    List<UserDto> getUsersByUsernames(List<String> usernames);
    List<UserDto> getAllActiveUsers();
    UpdateUserResponse updateUser(String userId, UpdateUserRequest updateUserRequest);
    UpdateUserByUsernameResponse updateUserByUsername(String username, UpdateUserRequest updateUserRequest);
    void deleteUser(String userId);
    DeleteUserByUsernameResponse deleteUserByUsername(String username);
    void changePasswordByUsername(String username, ChangePasswordRequest changePasswordRequest);
    void updateUserActiveStatus(String username, UpdateUserActiveStatusRequest updateUserActiveStatusRequest);
}
