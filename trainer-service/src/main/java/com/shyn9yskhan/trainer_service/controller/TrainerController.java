package com.shyn9yskhan.trainer_service.controller;

import com.shyn9yskhan.trainer_service.dto.*;
import com.shyn9yskhan.trainer_service.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<CreateTrainerResponse> createTrainer(@RequestBody CreateTrainerRequest createTrainerRequest) {
        CreateTrainerResponse response = trainerService.createTrainer(createTrainerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<GetTrainerResponse> getTrainer(@PathVariable String trainerId) {
        return ResponseEntity.ok(trainerService.getTrainer(trainerId));
    }

    @GetMapping("/{trainerId}/training-type")
    public ResponseEntity<GetTrainingTypeIdByTrainerIdResponse> getTrainingTypeIdByTrainerId(@PathVariable String trainerId) {
        return ResponseEntity.ok(trainerService.getTrainingTypeIdByTrainerId(trainerId));
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<GetTrainerResponse> getTrainerByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(trainerService.getTrainerByUserId(userId));
    }

    @GetMapping("/by-user-id/{userId}/id")
    public ResponseEntity<GetTrainerIdByUserIdResponse> getTrainerIdByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(trainerService.getTrainerIdByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<TrainerDto>> getTrainersByIds(@RequestParam List<String> trainerIds) {
        return ResponseEntity.ok(trainerService.getTrainersByIds(trainerIds));
    }

    @GetMapping
    public ResponseEntity<List<TrainerDto>> getTrainersByUserIds(@RequestParam List<String> userIds) {
        return ResponseEntity.ok(trainerService.getTrainersByUserIds(userIds));
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<UpdateTrainerResponse> updateTrainer(@PathVariable String trainerId, @RequestBody UpdateTrainerRequest updateTrainerRequest) {
        return ResponseEntity.ok(trainerService.updateTrainer(trainerId, updateTrainerRequest));
    }

    @PutMapping("/by-user-id/{userId}")
    public ResponseEntity<UpdateTrainerResponse> updateTrainerByUserId(@PathVariable String userId, @RequestBody UpdateTrainerRequest updateTrainerRequest) {
        return ResponseEntity.ok(trainerService.updateTrainerByUserId(userId, updateTrainerRequest));
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable String trainerId) {
        trainerService.deleteTrainer(trainerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-user-id/{userId}")
    public ResponseEntity<Void> deleteTrainerByUserId(@PathVariable String userId) {
        trainerService.deleteTrainerByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
