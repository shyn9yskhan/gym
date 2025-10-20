package com.shyn9yskhan.training_orchestration_service.client;

import com.shyn9yskhan.training_orchestration_service.client.dto.GetTraineeIdByUserIdServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TRAINEE-SERVICE")
public interface TraineeServiceClient {
    @GetMapping("/by-user-id/{userId}/id")
    ResponseEntity<GetTraineeIdByUserIdServiceResponse> getTraineeIdByUserId(@PathVariable String userId);
}
