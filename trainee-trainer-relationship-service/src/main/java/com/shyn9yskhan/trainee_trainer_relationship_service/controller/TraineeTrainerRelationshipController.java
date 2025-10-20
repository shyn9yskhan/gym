package com.shyn9yskhan.trainee_trainer_relationship_service.controller;

import com.shyn9yskhan.trainee_trainer_relationship_service.service.TraineeTrainerRelationshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee-trainer-relationship")
public class TraineeTrainerRelationshipController {
    private final TraineeTrainerRelationshipService traineeTrainerRelationshipService;

    public TraineeTrainerRelationshipController(TraineeTrainerRelationshipService traineeTrainerRelationshipService) {
        this.traineeTrainerRelationshipService = traineeTrainerRelationshipService;
    }

    @PutMapping("/trainee/{traineeId}/trainer/{trainerId}")
    public ResponseEntity<Void> associateTraineeTrainer(@PathVariable String traineeId, @PathVariable String trainerId) {
        traineeTrainerRelationshipService.associate(traineeId, trainerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/trainee/{traineeId}/trainers")
    public ResponseEntity<Void> associateTraineeWithTrainers(@PathVariable String traineeId, @RequestBody List<String> trainerIds) {
        traineeTrainerRelationshipService.associateTraineeWithTrainers(traineeId, trainerIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/trainee/{traineeId}/trainer/{trainerId}")
    public ResponseEntity<Void> disassociateTraineeTrainer(@PathVariable String traineeId, @PathVariable String trainerId) {
        traineeTrainerRelationshipService.disassociate(traineeId, trainerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/trainee/{traineeId}/trainer/{trainerId}")
    public ResponseEntity<Boolean> checkRelationshipExists(@PathVariable String traineeId, @PathVariable String trainerId) {
        return ResponseEntity.ok(traineeTrainerRelationshipService.exists(traineeId, trainerId));
    }

    @GetMapping("/trainee/{traineeId}/trainers")
    public ResponseEntity<List<String>> getTrainersForTrainee(@PathVariable String traineeId) {
        return ResponseEntity.ok(traineeTrainerRelationshipService.getTrainersForTrainee(traineeId));
    }

    @GetMapping("/trainer/{trainerId}/trainees")
    public ResponseEntity<List<String>> getTraineesForTrainer(@PathVariable String trainerId) {
        return ResponseEntity.ok(traineeTrainerRelationshipService.getTraineesForTrainer(trainerId));
    }
}
