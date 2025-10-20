package com.shyn9yskhan.authentication_service.client;

import com.shyn9yskhan.authentication_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    @GetMapping("/user/by-username/{username}")
    ResponseEntity<UserDto> getUserByUsername(@PathVariable String username);
}
