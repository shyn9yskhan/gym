package com.shyn9yskhan.user_orchestration_service.client;

import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "TRAINER-SERVICE")
public interface TrainerServiceClient {

    @PostMapping("/trainer")
    ResponseEntity<CreateTrainerServiceResponse> createTrainer(@RequestBody CreateTrainerServiceRequest createTrainerServiceRequest);

    @GetMapping("/trainer/{trainerId}")
    ResponseEntity<GetTrainerServiceResponse> getTrainer(@PathVariable String trainerId);

    @GetMapping("/trainer/by-ids")
    ResponseEntity<List<TrainerServiceDto>> getTrainersByIds(@RequestParam List<String> trainerIds);

    @GetMapping("/trainer/by-user-ids")
    ResponseEntity<List<TrainerServiceDto>> getTrainersByUserIds(@RequestParam List<String> userIds);

    @PutMapping("/trainer/{trainerId}")
    ResponseEntity<UpdateTrainerServiceResponse> updateTrainer(@PathVariable String trainerId, @RequestBody UpdateTrainerServiceRequest updateTrainerRequest);

    @DeleteMapping("/trainer/{trainerId}")
    ResponseEntity<Void> deleteTrainer(@PathVariable String trainerId);
}
