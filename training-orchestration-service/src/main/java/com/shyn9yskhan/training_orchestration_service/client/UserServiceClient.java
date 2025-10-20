package com.shyn9yskhan.training_orchestration_service.client;

import com.shyn9yskhan.training_orchestration_service.client.dto.GetUserIdByUsernameServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    @GetMapping("/by-username/{username}/id")
    ResponseEntity<GetUserIdByUsernameServiceResponse> getUserIdByUsername(@PathVariable String username);
}
