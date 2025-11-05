package com.shyn9yskhan.authentication_service.client;

import com.shyn9yskhan.authentication_service.dto.GetTraineeIdByUserIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TRAINEE-SERVICE")
public interface TraineeServiceClient {
    @GetMapping("/trainee/by-user-id/{userId}/id")
    ResponseEntity<GetTraineeIdByUserIdResponse> getTraineeIdByUserId(@PathVariable String userId);
}
