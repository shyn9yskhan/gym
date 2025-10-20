package com.shyn9yskhan.user_service.controller;

import com.shyn9yskhan.user_service.dto.*;
import com.shyn9yskhan.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        CreateUserResponse createUserResponse = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<GetUserByUsernameResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/by-username/{username}/id")
    public ResponseEntity<GetUserIdByUsernameResponse> getUserIdByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserIdByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsersByIds(@RequestParam List<String> ids) {
        return ResponseEntity.ok(userService.getUsersByIds(ids));
    }

    @GetMapping("/by-usernames")
    public ResponseEntity<List<UserDto>> getUsersByUsernames(@RequestParam List<String> usernames) {
        return ResponseEntity.ok(userService.getUsersByUsernames(usernames));
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getAllActiveUsers() {
        return ResponseEntity.ok(userService.getAllActiveUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(userId, updateUserRequest));
    }

    @PutMapping("/by-username/{username}")
    public ResponseEntity<UpdateUserByUsernameResponse> updateUserByUsername(@PathVariable String username, @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUserByUsername(username, updateUserRequest));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-username/{username}")
    public ResponseEntity<DeleteUserByUsernameResponse> deleteUserByUsername(@PathVariable String username) {
        DeleteUserByUsernameResponse deleteUserByUsernameResponse = userService.deleteUserByUsername(username);
        return ResponseEntity.ok(deleteUserByUsernameResponse);
    }

    @PutMapping("/by-username/{username}/password")
    public ResponseEntity<Void> changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePasswordByUsername(username, changePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/by-username/{username}")
    public ResponseEntity<Void> updateUserActiveStatus(@PathVariable String username, @RequestBody UpdateUserActiveStatusRequest updateUserActiveStatusRequest) {
        userService.updateUserActiveStatus(username, updateUserActiveStatusRequest);
        return ResponseEntity.noContent().build();
    }
}
