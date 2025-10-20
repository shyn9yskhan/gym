package com.shyn9yskhan.trainee_service.controller;

import com.shyn9yskhan.trainee_service.dto.*;
import com.shyn9yskhan.trainee_service.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee")
public class TraineeController {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<CreateTraineeResponse> createTrainee(@RequestBody CreateTraineeRequest createTraineeRequest) {
        CreateTraineeResponse response = traineeService.createTrainee(createTraineeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{traineeId}")
    public ResponseEntity<GetTraineeResponse> getTrainee(@PathVariable String traineeId) {
        return ResponseEntity.ok(traineeService.getTrainee(traineeId));
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<GetTraineeByUserIdResponse> getTraineeByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(traineeService.getTraineeByUserId(userId));
    }

    @GetMapping("/by-user-id/{userId}/id")
    public ResponseEntity<GetTraineeIdByUserIdResponse> getTraineeIdByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(traineeService.getTraineeIdByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<TraineeDto>> getTraineesByIds(@RequestParam List<String> traineeIds) {
        return ResponseEntity.ok(traineeService.getTraineesByIds(traineeIds));
    }

    @PutMapping("/{traineeId}")
    public ResponseEntity<UpdateTraineeResponse> updateTrainee(@PathVariable String traineeId, @RequestBody UpdateTraineeRequest updateTraineeRequest) {
        return ResponseEntity.ok(traineeService.updateTrainee(traineeId, updateTraineeRequest));
    }

    @PutMapping("/by-user-id/{userId}")
    public ResponseEntity<UpdateTraineeByUserIdResponse> updateTraineeByUserId(@PathVariable String userId, @RequestBody UpdateTraineeRequest updateTraineeRequest) {
        return ResponseEntity.ok(traineeService.updateTraineeByUserId(userId, updateTraineeRequest));
    }

    @DeleteMapping("/{traineeId}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String traineeId) {
        traineeService.deleteTrainee(traineeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-user-id/{userId}")
    public ResponseEntity<Void> deleteTraineeByUserId(@PathVariable String userId) {
        traineeService.deleteTraineeByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
