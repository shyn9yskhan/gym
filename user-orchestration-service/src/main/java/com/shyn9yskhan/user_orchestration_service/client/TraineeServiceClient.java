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

    @GetMapping("/trainee/{traineeId}")
    ResponseEntity<GetTraineeServiceResponse> getTrainee(@PathVariable String traineeId);

    @PutMapping("/trainee/{traineeId}")
    ResponseEntity<UpdateTraineeServiceResponse> updateTrainee(@PathVariable String traineeId, @RequestBody UpdateTraineeServiceRequest updateTraineeRequest);

    @DeleteMapping("/trainee/{traineeId}")
    ResponseEntity<Void> deleteTrainee(@PathVariable String traineeId);

    @GetMapping("/trainee")
    ResponseEntity<List<GetTraineeByIdResponse>> getTraineesByIds(@RequestParam List<String> traineeIds);
}
