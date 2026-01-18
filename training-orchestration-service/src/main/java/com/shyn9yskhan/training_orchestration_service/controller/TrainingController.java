package com.shyn9yskhan.training_orchestration_service.controller;

import com.shyn9yskhan.training_orchestration_service.dto.*;
import com.shyn9yskhan.training_orchestration_service.service.TrainingOrchestrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/training")
public class TrainingController {
    private final TrainingOrchestrationService trainingOrchestrationService;

    public TrainingController(TrainingOrchestrationService trainingOrchestrationService) {
        this.trainingOrchestrationService = trainingOrchestrationService;
    }

    @GetMapping("/trainee")
    public ResponseEntity<GetTraineeTrainingsListResponse> getTraineeTrainingsList(@RequestHeader("X-User-Role") String role,
                                                                                   @RequestHeader("X-User-Second-Id") String traineeId,
                                                                                   @Valid @RequestBody GetTraineeTrainingsListRequest getTraineeTrainingsListRequest) {
        if (!"TRAINEE".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        GetTraineeTrainingsListResponse getTraineeTrainingsListResponse = trainingOrchestrationService.getTraineeTrainingsList(traineeId, getTraineeTrainingsListRequest);
        return ResponseEntity.ok(getTraineeTrainingsListResponse);
    }

    @GetMapping("/trainer")
    public ResponseEntity<GetTrainerTrainingsListResponse> getTrainerTrainingsList(@RequestHeader("X-User-Role") String role,
                                                                                   @RequestHeader("X-User-Second-Id") String trainerId,
                                                                                   @RequestBody GetTrainerTrainingsListRequest getTrainerTrainingsListRequest) {
        if (!"TRAINER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        GetTrainerTrainingsListResponse getTrainerTrainingsListResponse = trainingOrchestrationService.getTrainerTrainingsList(trainerId, getTrainerTrainingsListRequest);
        return ResponseEntity.ok(getTrainerTrainingsListResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addTraining(@RequestBody AddTrainingRequest addTrainingRequest) {
        trainingOrchestrationService.addTraining(addTrainingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
