package com.shyn9yskhan.user_orchestration_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "TRAINEE-TRAINER-RELATIONSHIP-SERVICE")
public interface TraineeTrainerRelationshipServiceClient {
    @GetMapping("/trainee-trainer-relationship/trainee/{traineeId}/trainers")
    ResponseEntity<List<String>> getTrainersForTrainee(@PathVariable String traineeId);

    @GetMapping("/trainer/{trainerId}/trainees")
    ResponseEntity<List<String>> getTraineesForTrainer(@PathVariable String trainerId);

    @PutMapping("/trainee/{traineeId}/trainers")
    ResponseEntity<Void> associateTraineeWithTrainers(@PathVariable String traineeId, @RequestBody List<String> trainerIds);
}
