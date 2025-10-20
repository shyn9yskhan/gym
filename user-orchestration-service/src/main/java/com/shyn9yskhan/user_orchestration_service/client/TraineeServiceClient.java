package com.shyn9yskhan.user_orchestration_service.client;

import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "TRAINEE-SERVICE")
public interface TraineeServiceClient {
    @PostMapping("/trainee")
    ResponseEntity<CreateTraineeServiceResponse> createTrainee(@RequestBody CreateTraineeServiceRequest createTraineeServiceRequest);

    @GetMapping("/trainee/by-user-id/{userId}")
    ResponseEntity<GetTraineeByUserIdServiceResponse> getTraineeByUserId(@PathVariable String userId);

    @PutMapping("/trainee/by-user-id/{userId}")
    ResponseEntity<UpdateTraineeByUserIdServiceResponse> updateTraineeByUserId(@PathVariable String userId, @RequestBody UpdateTraineeServiceRequest updateTraineeRequest);

    @DeleteMapping("/trainee/by-user-id/{userId}")
    ResponseEntity<Void> deleteTraineeByUserId(@PathVariable String userId);

    @GetMapping
    ResponseEntity<List<GetTraineeByIdResponse>> getTraineesByIds(@RequestParam List<String> traineeIds);
}
