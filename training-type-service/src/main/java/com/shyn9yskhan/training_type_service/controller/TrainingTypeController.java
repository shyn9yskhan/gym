package com.shyn9yskhan.training_type_service.controller;

import com.shyn9yskhan.training_type_service.dto.*;
import com.shyn9yskhan.training_type_service.service.TrainingTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training-type")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @PostMapping
    public ResponseEntity<CreateTrainingTypeResponse> createTrainingType(@RequestBody CreateTrainingTypeRequest createTrainingTypeRequest) {
        CreateTrainingTypeResponse response = trainingTypeService.createTrainingType(createTrainingTypeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{trainingTypeId}")
    public ResponseEntity<GetTrainingTypeResponse> getTrainingType(@PathVariable String trainingTypeId) {
        return ResponseEntity.ok(trainingTypeService.getTrainingType(trainingTypeId));
    }

    @DeleteMapping("/{trainingTypeId}")
    public ResponseEntity<Void> deleteTrainingType(@PathVariable String trainingTypeId) {
        trainingTypeService.deleteTrainingType(trainingTypeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<GetAllTrainingTypesResponse> getAllTrainingTypes() {
        return ResponseEntity.ok(trainingTypeService.getAllTrainingTypes());
    }
}
