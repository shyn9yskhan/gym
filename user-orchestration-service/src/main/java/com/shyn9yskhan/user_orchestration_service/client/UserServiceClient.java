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

    @GetMapping("/user/{userId}")
    ResponseEntity<GetUserServiceResponse> getUser(@PathVariable String userId);

    @GetMapping("/user")
    ResponseEntity<List<UserDto>> getUsersByIds(@RequestParam List<String> ids);

    @GetMapping("/user")
    ResponseEntity<List<UserDto>> getUsersByUsernames(@RequestParam List<String> usernames);

    @GetMapping("/user/active")
    ResponseEntity<List<UserDto>> getAllActiveUsers();

    @PutMapping("/user/{userId}")
    ResponseEntity<UpdateUserServiceResponse> updateUser(@PathVariable String userId, @RequestBody UpdateUserServiceRequest updateUserRequest);

    @DeleteMapping("/user/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable String userId);
}
