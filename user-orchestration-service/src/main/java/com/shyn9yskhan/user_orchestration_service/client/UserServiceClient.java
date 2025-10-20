package com.shyn9yskhan.user_orchestration_service.client;

import com.shyn9yskhan.user_orchestration_service.client.dto.user.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    @PostMapping("/user")
    ResponseEntity<CreateUserServiceResponse> createUser(@RequestBody CreateUserServiceRequest createUserServiceRequest);

    @GetMapping("/user/by-username/{username}")
    ResponseEntity<GetUserServiceResponse> getUserByUsername(@PathVariable String username);

    @GetMapping("/user")
    ResponseEntity<List<UserDto>> getUsersByIds(@RequestParam List<String> userIds);

    @GetMapping
    ResponseEntity<List<UserDto>> getUsersByUsernames(@RequestParam List<String> usernames);

    @GetMapping("/active")
    ResponseEntity<List<UserDto>> getAllActiveUsers();

    @PutMapping("/user/by-username/{username}")
    ResponseEntity<UpdateUserByUsernameServiceResponse> updateUserByUsername(@PathVariable String username, @RequestBody UpdateUserServiceRequest updateUserRequest);

    @DeleteMapping("/user/by-username/{username}")
    ResponseEntity<DeleteUserByUsernameServiceResponse> deleteUserByUsername(@PathVariable String username);
}
