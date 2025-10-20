package com.shyn9yskhan.training_orchestration_service.client;

import com.shyn9yskhan.training_orchestration_service.client.dto.CreateTrainingServiceRequest;
import com.shyn9yskhan.training_orchestration_service.client.dto.CreateTrainingServiceResponse;
import com.shyn9yskhan.training_orchestration_service.client.dto.GetTraineeTrainingsServiceResponse;
import com.shyn9yskhan.training_orchestration_service.client.dto.GetTrainerTrainingsServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TRAINING-SERVICE")
public interface TrainingServiceClient {
    @GetMapping("/trainee/{traineeId}")
    ResponseEntity<GetTraineeTrainingsServiceResponse> getTrainingsByTraineeId(@PathVariable String traineeId);

    @GetMapping("/trainer/{trainerId}")
    ResponseEntity<GetTrainerTrainingsServiceResponse> getTrainingsByTrainerId(@PathVariable String trainerId);

    @PostMapping
    ResponseEntity<CreateTrainingServiceResponse> createTraining(@RequestBody CreateTrainingServiceRequest createTrainingRequest);
}
