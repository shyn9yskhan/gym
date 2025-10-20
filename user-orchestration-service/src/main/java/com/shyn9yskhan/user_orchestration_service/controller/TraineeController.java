package com.shyn9yskhan.user_orchestration_service.controller;

import com.shyn9yskhan.user_orchestration_service.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.service.UserOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainee")
public class TraineeController {
    private final UserOrchestrationService userOrchestrationService;

    public TraineeController(UserOrchestrationService userOrchestrationService) {
        this.userOrchestrationService = userOrchestrationService;
    }

    @PostMapping
    public ResponseEntity<CreateTraineeResponse> createTrainee(@RequestBody CreateTraineeRequest createTraineeRequest) {
        CreateTraineeResponse createTraineeResponse = userOrchestrationService.createTrainee(createTraineeRequest);
        return ResponseEntity.ok(createTraineeResponse);
    }

    @GetMapping
    public ResponseEntity<GetTraineeProfileResponse> getTraineeProfile(@RequestBody String username) {
        GetTraineeProfileResponse getTraineeProfileResponse = userOrchestrationService.getTraineeProfile(username);
        return ResponseEntity.ok(getTraineeProfileResponse);
    }

    @PutMapping
    public ResponseEntity<UpdateTraineeProfileResponse> updateTraineeProfile(@RequestBody UpdateTraineeProfileRequest updateTraineeProfileRequest) {
        UpdateTraineeProfileResponse updateTraineeProfileResponse = userOrchestrationService.updateTraineeProfile(updateTraineeProfileRequest);
        return ResponseEntity.ok(updateTraineeProfileResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTrainee(@RequestBody String username) {
        userOrchestrationService.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }
}
