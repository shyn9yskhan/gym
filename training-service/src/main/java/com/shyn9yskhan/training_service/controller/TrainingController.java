package com.shyn9yskhan.training_service.controller;

import com.shyn9yskhan.training_service.dto.*;
import com.shyn9yskhan.training_service.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<CreateTrainingResponse> createTraining(@RequestBody CreateTrainingRequest createTrainingRequest) {
        CreateTrainingResponse createTrainingResponse = trainingService.createTraining(createTrainingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTrainingResponse);
    }

    @GetMapping("/{trainingId}")
    public ResponseEntity<GetTrainingResponse> getTraining(@PathVariable String trainingId) {
        GetTrainingResponse getTrainingResponse = trainingService.getTraining(trainingId);
        return ResponseEntity.ok(getTrainingResponse);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<UpdateTrainingResponse> updateTraining(@PathVariable String trainingId, @RequestBody UpdateTrainingRequest updateTrainingRequest) {
        UpdateTrainingResponse updateTrainingResponse = trainingService.updateTraining(trainingId, updateTrainingRequest);
        return ResponseEntity.ok(updateTrainingResponse);
    }

    @DeleteMapping("/{trainingId}")
    public ResponseEntity<Void> deleteTraining(@PathVariable String trainingId) {
        trainingService.deleteTraining(trainingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trainee/{traineeId}")
    public ResponseEntity<GetTraineeTrainingsResponse> getTrainingsByTraineeId(@PathVariable String traineeId) {
        GetTraineeTrainingsResponse response = trainingService.getTrainingsByTraineeId(traineeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<GetTrainerTrainingsResponse> getTrainingsByTrainerId(@PathVariable String trainerId) {
        GetTrainerTrainingsResponse response = trainingService.getTrainingsByTrainerId(trainerId);
        return ResponseEntity.ok(response);
    }
}
