package com.shyn9yskhan.user_orchestration_service.client;

import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "TRAINER-SERVICE")
public interface TrainerServiceClient {

    @PostMapping
    ResponseEntity<CreateTrainerServiceResponse> createTrainer(@RequestBody CreateTrainerServiceRequest createTrainerServiceRequest);

    @GetMapping("/by-user-id/{userId}")
    ResponseEntity<TrainerServiceDto> getTrainerByUserId(@PathVariable String userId);

    @GetMapping("/trainer")
    ResponseEntity<List<TrainerServiceDto>> getTrainersByIds(@RequestParam List<String> trainerIds);

    @GetMapping
    ResponseEntity<List<TrainerServiceDto>> getTrainersByUserIds(@RequestParam List<String> userIds);

    @PutMapping("/by-user-id/{userId}")
    ResponseEntity<UpdateTrainerByUserIdServiceResponse> updateTrainerByUserId(@PathVariable String userId, @RequestBody UpdateTrainerServiceRequest updateTrainerServiceRequest);

    @DeleteMapping("/by-user-id/{userId}")
    ResponseEntity<Void> deleteTrainerByUserId(@PathVariable String userId);
}
