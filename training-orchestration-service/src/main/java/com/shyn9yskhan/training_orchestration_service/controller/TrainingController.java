package com.shyn9yskhan.training_orchestration_service.controller;

import com.shyn9yskhan.training_orchestration_service.dto.*;
import com.shyn9yskhan.training_orchestration_service.service.TrainingOrchestrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
public class TrainingController {
    private final TrainingOrchestrationService trainingOrchestrationService;

    public TrainingController(TrainingOrchestrationService trainingOrchestrationService) {
        this.trainingOrchestrationService = trainingOrchestrationService;
    }

    @GetMapping("/trainee")
    public ResponseEntity<GetTraineeTrainingsListResponse> getTraineeTrainingsList(@Valid @RequestBody GetTraineeTrainingsListRequest getTraineeTrainingsListRequest) {
        GetTraineeTrainingsListResponse getTraineeTrainingsListResponse = trainingOrchestrationService.getTraineeTrainingsList(getTraineeTrainingsListRequest);
        return ResponseEntity.ok(getTraineeTrainingsListResponse);
    }

    @GetMapping("/trainer")
    public ResponseEntity<GetTrainerTrainingsListResponse> getTrainerTrainingsList(@RequestBody GetTrainerTrainingsListRequest getTrainerTrainingsListRequest) {
        GetTrainerTrainingsListResponse getTrainerTrainingsListResponse = trainingOrchestrationService.getTrainerTrainingsList(getTrainerTrainingsListRequest);
        return ResponseEntity.ok(getTrainerTrainingsListResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addTraining(@RequestBody AddTrainingRequest addTrainingRequest) {
        trainingOrchestrationService.addTraining(addTrainingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
