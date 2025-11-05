package com.shyn9yskhan.authentication_service.client;

import com.shyn9yskhan.authentication_service.dto.GetTrainerIdByUserIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TRAINER-SERVICE")
public interface TrainerServiceClient {
    @GetMapping("/trainer/by-user-id/{userId}/id")
    ResponseEntity<GetTrainerIdByUserIdResponse> getTrainerIdByUserId(@PathVariable String userId);
}
